package org.woped.editor.controller.vc;

public class IdGenerator {
	private static long id = 0;

    public static synchronized String generateId(String prefix) {
        return (prefix + ++id);
    }

}
