package org.woped.qualanalysis.algorithms;

import java.io.File;

public class Woflan {
    public native int Open(String theFileName);
    public native int Close(int theNet);
    public native String Info(int theNet, int theInfo, int theIndex, int theSubIndex);
    
    public String SCTransition = "t*";
    public String SilentLabel = " t";
    
    // Valid values for theInfo (these should match the values as defined in woflan.h)
    
    //         Mnemonic             Value   SC? Explanation
    
    // Basic
    public int InfoName          =    1; // No  Name
    public int InfoNofP          =    2; // No  Number of places
    public int InfoNofT          =    3; // No  Number of transitions
    public int InfoNofC          =    4; // No  Number of arcs (connections)
    public int InfoPName         =    5; // No  Name of place [theIndex]
    public int InfoTName         =    6; // No  Name of transition [theIndex]
    public int InfoNofPreP       =    7; // No  Size of preset of place [theIndex]
    public int InfoNofPostP      =    8; // No  Size of postset of place [theIndex]
    public int InfoNamePreP      =    9; // No  Name of preset [theSubIndex] of place [theIndex]
    public int InfoNamePostP     =   10; // No  Name of postset [theSubIndex] of place [theIndex]
    
    // WF-net
    public int InfoNofSrcP       =  101; // No  Number of source places
    public int InfoNofSnkP       =  102; // No  Number of sink places
    public int InfoNofSrcT       =  103; // No  Number of source transitions
    public int InfoNofSnkT       =  104; // No  Number of sink transitions
    public int InfoNofUncN       =  105; // Yes Number of nodes not connected to SCTransition
    public int InfoNofSncN       =  106; // Yes Number of nodes not strongly connected to SCTransition
    public int InfoSrcPName      =  107; // No  Name of source place [theIndex]
    public int InfoSnkPName      =  108; // No  Name of sink place [theIndex]
    public int InfoSrcTName      =  109; // No  Name of source transition [theIndex]
    public int InfoSnkTName      =  110; // No  Name of sink transition [theIndex]
    public int InfoUncNName      =  111; // Yes Name of node [theIndex] not connected to SCTransition
    public int InfoSncNName      =  112; // Yes Name of node [theIndex] not strongly connected to SCTransition
    public int SetSUnc           =  113; // Yes Initialize (stronlgy) connected info

    // Non-free-choice
    public int SetNFCC           =  150; // No  Set non-free-choice-cluster info
    public int InfoNofNFCC       =  151; // No  Number of non-free-chocie clusters
    public int InfoNFCCNofN      =  152; // No  Number of nodes in non-free-choice-cluster [theIndex]
    public int InfoNFCCNName     =  153; // No  Name of node [theSubIndex] in non-free-choice-cluster [theIndex]

    // PT-handles 
    public int SetPTH            =  200; // Yes Set handles info (both PT and TP)
    public int InfoNofPTH        =  201; // Yes Number of PT-handles
    public int InfoPTHNofN1      =  202; // Yes Number of nodes on some path for PT-handle [theIndex]
    public int InfoPTHNofN2      =  203; // Yes Number of nodes on alternative path for PT-handle [theIndex]
    public int InfoPTHN1Name     =  204; // Yes Name of node [theSubIndex] on some path for PT-handle [theIndex]
    public int InfoPTHN2Name     =  205; // Yes Name of node [theSubIndex] on alternative path for PT-handle [theIndex]
    
    // TP-handles 
    public int SetTPH            =  300; // Yes Set handles info (both PT and TP)
    public int InfoNofTPH        =  301; // Yes Number of TP-handles
    public int InfoTPHNofN1      =  302; // Yes Number of nodes on some path for TP-handle [theIndex]
    public int InfoTPHNofN2      =  303; // Yes Number of nodes on alternative path for TP-handle [theIndex]
    public int InfoTPHN1Name     =  304; // Yes Name of node [theSubIndex] on some path for TP-handle [theIndex]
    public int InfoTPHN2Name     =  305; // Yes Name of node [theSubIndex] on alternative path for TP-handle [theIndex]
    
    // S-Components
    public int SetSCom           =  400; // No  Set S-Components info
    public int InfoNofSCom       =  401; // No  Number of S-Components
    public int InfoNofNotSCom    =  402; // No  Number of nodes not covered by S-Components
    public int InfoSComNofN      =  403; // No  Number of nodes in S-Component [theIndex]
    public int InfoSComNName     =  404; // No  Name of node [theSubIndex] in S-Component [theIndex]
    public int InfoNotSComNName  =  405; // No  Name of node [theIndex] not covered by S-Components
    
    // P-Invariants
    public int SetPInv           =  500; // No  Set P-Invariants info
    public int InfoNofPInv       =  501; // No  Number of P-Invariants
    public int InfoNofPotPInv    =  502; // No  Number of places not covered by P-Invariants
    public int InfoPInvNofP      =  503; // No  Number of places in P-Invariant [theIndex]
    public int InfoPInvPName     =  504; // No  Name of place [theSubIndex] in P-Invariant [theIndex]
    public int InfoPInvPWeight   =  505; // No  Weight of place [theSubIndex] in P-Invariant [theIndex]
    public int InfoNotPInvPName  =  506; // No  Name of place [theIndex] not covered by P-Invariants
    
    // Semi-positive P-Invariants
    public int SetSPIn           =  600; // No  Set semi-positive P-Invariants info
    public int InfoNofSPIn       =  601; // No  Number of semi-positive P-Invariants
    public int InfoNofPotSPIn    =  602; // No  Number of places not covered by semi-positive P-Invariants
    public int InfoSPInNofP      =  603; // No  Number of places in semi-positive P-Invariant [theIndex]
    public int InfoSPInPName     =  604; // No  Name of place [theSubIndex] in semi-positive P-Invariant [theIndex]
    public int InfoSPInPWeight   =  605; // No  Weight of place [theSubIndex] in semi-positive P-Invariant [theIndex]
    public int InfoNotSPInPName  =  606; // No  Name of place [theIndex] not covered by semi-positive P-Invariants
    
    // T-Invariants
    public int SetTInv           =  700; // Yes Set T-Invariants info
    public int InfoNofTInv       =  701; // Yes Number of T-Invariants
    public int InfoNofPotTInv    =  702; // Yes Number of transitions not covered by T-Invariants
    public int InfoTInvNofP      =  703; // Yes Number of transitions in T-Invariant [theIndex]
    public int InfoTInvPName     =  704; // Yes Name of transition [theSubIndex] in T-Invariant [theIndex]
    public int InfoTInvPWeight   =  705; // Yes Weight of transition [theSubIndex] in T-Invariant [theIndex]
    public int InfoNotTInvPName  =  706; // Yes Name of transition [theIndex] not covered by T-Invariants
    
    // Semi-positive T-Invariants
    public int SetSTIn           =  800; // Yes Set semi-positive T-Invariants info
    public int InfoNofSTIn       =  801; // Yes Number of semi-positive T-Invariants
    public int InfoNofPotSTIn    =  802; // Yes Number of transitions not covered by semi-positive T-Invariants
    public int InfoSTInNofP      =  803; // Yes Number of transitions in semi-positive T-Invariant [theIndex]
    public int InfoSTInPName     =  804; // Yes Name of transition [theSubIndex] in semi-positive T-Invariant [theIndex]
    public int InfoSTInPWeight   =  805; // Yes Weight of transition [theSubIndex] in semi-positive T-Invariant [theIndex]
    public int InfoNotSTInPName  =  806; // Yes Name of transition [theIndex] not covered by semi-positive T-Invariants

    // Unboudedness
    public int SetUnb            =  900; // Yes Set unboundedness info
    public int InfoNofUnbP       =  901; // Yes Number of unbounded places
    public int InfoUnbPName      =  902; // Yes Name of unbounded place [theIndex]
    public int InfoNofUnbS       =  903; // Yes Number of unbounded sequences
    public int InfoUnbSNofT      =  904; // Yes Number of transitions in unbounded sequence [theIndex]
    public int InfoUnbSTName     =  905; // Yes Name of transition [theSubIndex] in unbounded sequence [theIndex]

    // Non-liveness
    public int SetNLive          = 1000; // Yes Set non-liveness info
    public int InfoNofDeadT      = 1001; // Yes Number of dead transitions
    public int InfoDeadTName     = 1002; // Yes Name of dead transition [theIndex]
    public int InfoNofNLiveT     = 1003; // Yes Number of non-live transitions
    public int InfoNLiveTName    = 1004; // Yes Name of non-live transition [theIndex]
    public int InfoNofNLiveS     = 1005; // Yes Number of non-live sequences
    public int InfoNLiveSNofT    = 1006; // Yes Number of transitions in non-live sequence [theIndex]
    public int InfoNLiveSTName   = 1007; // Yes Name of transition [theSubIndex] in non-live sequence [theIndex]

    // Inheritance
    public int SetInh            = 1100; // No  Set inheritance info using net theIndex as base net and this net as potential subnet
    public int InfoInhPT         = 1101; // No  Protocol inheritance? (returns "0" if not)
    public int InfoInhPJ         = 1102; // No  Projection inheritance? (returns "0" if not)
    public int InfoInhPP         = 1103; // No  Protocol/Projection inheritance? (returns "0" if not)
    public int InfoInhLC         = 1104; // No  Life-Cycle inheritance? (returns "0" if not)
    // Note: InfoInhLC uses backtracking algorithm, but this depends on the existence of semi-positive T-Invariants for the short-circuited net.
    // Thus: Invoking InfoInhLC will result in an effective backtracking algorithm iff SetSTIn has been invoked beforehand.

    // Murata reduction rules
    public int SetMurata         = 1200; // Bth Apply the well-known Murata reduction rules to this net (and its short-circuited net)

    static {
        try {
            String path = Class.class.getResource("/org").toString();
            if (path.indexOf("jar:file:") != -1) {
                String fn = path.replaceAll("jar:file:/", "");
                // find jar start
                int n = fn.indexOf("!");
                if (n == -1) n = fn.length();
                // read Jar File Name
                fn = fn.substring(0, n);
                // Replace all whitespaces in filename
                fn = fn.replaceAll("%20", " ");
                int i;
                String fn1 = "";
                for (i = fn.length()-1; fn.charAt(i) != '/'; i--);
                fn = fn.substring(0, i);
                fn += File.separator;
        		for (i = 0; i < fn.length(); i++) {
        			if (fn.charAt(i) == '/') {
        				fn1 += File.separator;
        			} else
        				fn1 += fn.charAt(i);
        		}

                path = fn1 + ";" + System.getProperty("java.library.path", ".");
                System.setProperty("java.library.path", path);
            }
            
            System.loadLibrary("WofJava");
        } catch (Exception ex) {
            System.err.println("Error while loading Woflan: " + ex.getMessage());
        }
    }
} 