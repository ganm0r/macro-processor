package main;

import java.io.IOException;

import macro.TwoPassMacro;

public class Main {
    public static void main(String[] args) throws IOException {
        final String INPUT_FILE_NAME = "/home/gndhrv/Documents/Sem VI/SPCC/Macro/lib/inputFile.txt";
        final String OUTPUT_FILE_NAME = "/home/gndhrv/Documents/Sem VI/SPCC/Macro/lib/outputFile.txt";

        TwoPassMacro twoPassMacro = new TwoPassMacro();
        twoPassMacro.simulate(INPUT_FILE_NAME, OUTPUT_FILE_NAME);
    }
}



