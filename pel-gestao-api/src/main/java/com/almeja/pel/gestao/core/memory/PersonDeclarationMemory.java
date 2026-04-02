package com.almeja.pel.gestao.core.memory;

import java.util.HashMap;
import java.util.Map;

public class PersonDeclarationMemory {

    private static PersonDeclarationMemory instance;

    private final Map<String, byte[]> declarationsMap = new HashMap<>();

    private PersonDeclarationMemory() {
    }

    public static PersonDeclarationMemory getInstance() {
        if (instance == null) {
            instance = new PersonDeclarationMemory();
        }
        return instance;
    }

    public void saveDeclaration(String cpf, byte[] declarationPdf) {
        declarationsMap.put(cpf, declarationPdf);
    }

    public byte[] getDeclaration(String cpf) {
        return declarationsMap.get(cpf);
    }
}
