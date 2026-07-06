package org.woped.core.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Initializes a merged SSL truststore at application startup so HTTPS connections to
 * DHBW services (e.g. Text2Process LLM on port 443) work without manual certificate imports.
 */
public final class SslTrustStoreInitializer {

    private static final String BUNDLED_TRUSTSTORE_RESOURCE = "/woped-truststore.jks";
    private static final char[] BUNDLED_TRUSTSTORE_PASSWORD = "woped123".toCharArray();
    private static final String[] PEM_CERT_RESOURCES = {
        "/ssl/geant_tls_rsa_1.pem",
        "/ssl/geant_ov_rsa_ca_4.pem",
        "/ssl/harica_tls_rsa_root_ca_2021.pem"
    };

    private static boolean initialized;

    private SslTrustStoreInitializer() {}

    public static synchronized void initialize() {
        if (initialized) {
            return;
        }

        System.setProperty("com.sun.security.enableAIAcaIssuers", "true");
        System.setProperty("com.sun.net.ssl.checkRevocation", "false");

        try {
            KeyStore mergedTrustStore = loadDefaultTrustStore();
            mergeBundledTrustStore(mergedTrustStore);
            mergeBundledPemCertificates(mergedTrustStore);
            mergeWindowsRootCertificates(mergedTrustStore);
            mergeUserCertificates(mergedTrustStore);
            applyTrustStore(mergedTrustStore);
            initialized = true;
            System.out.println("SSL truststore initialized with " + mergedTrustStore.size() + " entries");
        } catch (Exception e) {
            System.err.println("Failed to initialize SSL truststore: " + e.getMessage());
        }
    }

    private static KeyStore loadDefaultTrustStore() throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

        String configuredPath = System.getProperty("javax.net.ssl.trustStore");
        if (configuredPath != null && !configuredPath.isEmpty()) {
            char[] password = System.getProperty("javax.net.ssl.trustStorePassword", "changeit")
                    .toCharArray();
            try (InputStream inputStream = new FileInputStream(configuredPath)) {
                trustStore.load(inputStream, password);
            }
            return trustStore;
        }

        File cacerts = new File(System.getProperty("java.home"), "lib/security/cacerts");
        if (!cacerts.isFile()) {
            cacerts = new File(System.getProperty("java.home"), "jre/lib/security/cacerts");
        }

        try (InputStream inputStream = new FileInputStream(cacerts)) {
            trustStore.load(inputStream, "changeit".toCharArray());
        }
        return trustStore;
    }

    private static void mergeBundledTrustStore(KeyStore mergedTrustStore) throws Exception {
        InputStream truststoreStream =
                SslTrustStoreInitializer.class.getResourceAsStream(BUNDLED_TRUSTSTORE_RESOURCE);
        if (truststoreStream == null) {
            return;
        }

        try (InputStream inputStream = truststoreStream) {
            KeyStore bundledTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            bundledTrustStore.load(inputStream, BUNDLED_TRUSTSTORE_PASSWORD);
            mergeKeyStores(mergedTrustStore, bundledTrustStore, "bundled-jks-");
        }
    }

    private static void mergeBundledPemCertificates(KeyStore mergedTrustStore) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

        for (String resourcePath : PEM_CERT_RESOURCES) {
            InputStream pemStream = SslTrustStoreInitializer.class.getResourceAsStream(resourcePath);
            if (pemStream == null) {
                continue;
            }

            try (InputStream inputStream = pemStream) {
                addCertificatesToTrustStore(
                        mergedTrustStore,
                        parsePemCertificates(
                                new String(inputStream.readAllBytes(), StandardCharsets.US_ASCII),
                                certificateFactory),
                        "bundled-pem-");
            }
        }
    }

    private static void mergeWindowsRootCertificates(KeyStore mergedTrustStore) {
        if (!System.getProperty("os.name", "").toLowerCase().contains("windows")) {
            return;
        }

        try {
            KeyStore windowsRoot = KeyStore.getInstance("Windows-ROOT", "SunMSCAPI");
            windowsRoot.load(null, null);
            mergeKeyStores(mergedTrustStore, windowsRoot, "windows-root-");
        } catch (Exception e) {
            System.err.println("Failed to merge Windows root certificates: " + e.getMessage());
        }
    }

    private static void mergeUserCertificates(KeyStore mergedTrustStore) throws Exception {
        File userCertDirectory = new File(System.getProperty("user.home"), ".WoPeD/ssl");
        if (!userCertDirectory.isDirectory()) {
            return;
        }

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        File[] certificateFiles = userCertDirectory.listFiles(
                file -> file.isFile() && hasCertificateExtension(file.getName()));
        if (certificateFiles == null) {
            return;
        }

        for (File certificateFile : certificateFiles) {
            try (InputStream inputStream = new FileInputStream(certificateFile)) {
                if (certificateFile.getName().toLowerCase().endsWith(".pem")
                        || certificateFile.getName().toLowerCase().endsWith(".crt")) {
                    addCertificatesToTrustStore(
                            mergedTrustStore,
                            parsePemCertificates(
                                    new String(inputStream.readAllBytes(), StandardCharsets.US_ASCII),
                                    certificateFactory),
                            "user-pem-" + certificateFile.getName() + "-");
                } else {
                    addCertificatesToTrustStore(
                            mergedTrustStore,
                            List.of(certificateFactory.generateCertificate(inputStream)),
                            "user-der-" + certificateFile.getName() + "-");
                }
            } catch (Exception e) {
                System.err.println(
                        "Failed to load user certificate " + certificateFile.getName() + ": " + e.getMessage());
            }
        }
    }

    private static boolean hasCertificateExtension(String fileName) {
        String lowerCaseName = fileName.toLowerCase();
        return lowerCaseName.endsWith(".pem")
                || lowerCaseName.endsWith(".crt")
                || lowerCaseName.endsWith(".cer");
    }

    private static void addCertificatesToTrustStore(
            KeyStore mergedTrustStore, List<Certificate> certificates, String aliasPrefix) throws Exception {
        for (Certificate certificate : certificates) {
            String alias = aliasPrefix + certificate.hashCode();
            if (!mergedTrustStore.containsAlias(alias)) {
                mergedTrustStore.setCertificateEntry(alias, certificate);
            }
        }
    }

    private static List<Certificate> parsePemCertificates(
            String pemContent, CertificateFactory certificateFactory) throws Exception {
        List<Certificate> certificates = new ArrayList<>();
        String normalized = pemContent.replace("\r\n", "\n");
        int startIndex = 0;

        while (true) {
            int beginMarker = normalized.indexOf("-----BEGIN CERTIFICATE-----", startIndex);
            if (beginMarker < 0) {
                break;
            }
            int endMarker = normalized.indexOf("-----END CERTIFICATE-----", beginMarker);
            if (endMarker < 0) {
                break;
            }

            String pemBlock = normalized.substring(beginMarker, endMarker + "-----END CERTIFICATE-----".length());
            try (InputStream certStream = new java.io.ByteArrayInputStream(
                    pemBlock.getBytes(StandardCharsets.US_ASCII))) {
                certificates.add(certificateFactory.generateCertificate(certStream));
            }
            startIndex = endMarker + "-----END CERTIFICATE-----".length();
        }

        return certificates;
    }

    private static void mergeKeyStores(KeyStore target, KeyStore source, String aliasPrefix) throws Exception {
        java.util.Enumeration<String> aliases = source.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (!source.isCertificateEntry(alias)) {
                continue;
            }
            Certificate certificate = source.getCertificate(alias);
            String mergedAlias = aliasPrefix + alias;
            if (!target.containsAlias(mergedAlias)) {
                target.setCertificateEntry(mergedAlias, certificate);
            }
        }
    }

    private static void applyTrustStore(KeyStore trustStore) throws Exception {
        TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }
}
