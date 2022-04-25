package macro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import datastructures.ALA1;
import datastructures.MDT;
import datastructures.MNT;

public class TwoPassMacro {

    public void simulate(String sourceCodeFile, String outputFile) throws IOException, FileNotFoundException {

        Vector<MDT> mdt = new Vector<MDT>();
        Vector<MNT> mnt = new Vector<MNT>();
        Vector<String> intermediateFile = new Vector<String>();
        Vector<ALA1> ala1 = new Vector<ALA1>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceCodeFile));

        String currentLine;
        int mdtc = 0, alac = 0;

        while((currentLine = bufferedReader.readLine()) != null) {
            if(currentLine.equals("MACRO")) {
                currentLine = bufferedReader.readLine();

                String[] splitCurrentLine = currentLine.split(" ");
                String[] splitArgsForMacro = splitCurrentLine[1].split(",");

                mnt.addElement(new MNT(splitCurrentLine[0], splitArgsForMacro.length , alac, mdtc));
                alac += splitArgsForMacro.length;

                for(int i = 0; i < splitArgsForMacro.length; i++) {
                    ala1.addElement(new ALA1(splitCurrentLine[0], i + 1, splitArgsForMacro[i]));
                }

                while(true) {

                    currentLine = bufferedReader.readLine();

                    mdt.addElement(new MDT(mdtc, currentLine));
                    mdtc++;

                    if(currentLine.equals("MEND")) {
                        break;
                    }
                }

                
            } else {
                intermediateFile.addElement(currentLine);
            }
        }

        bufferedReader.close();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));

        bufferedWriter.write("Two Pass Macro Processor:\n\nPass One:\n\nMacro Name Table:\n\n");

        for(int i = 0; i < mnt.size(); i++) {
            String currentOutputLine = i + " " + mnt.get(i).name + " " + mnt.get(i).paramCount + " " + mnt.get(i).alac + " " + mnt.get(i).mdtc;

            bufferedWriter.write(currentOutputLine);
            bufferedWriter.newLine();
        }

        bufferedWriter.write("\nMacro Definition Table:\n\n");

        for(int i = 0; i < mdt.size(); i++) {
            String currentOutputLine = mdt.get(i).mdtc + " " + mdt.get(i).definition;

            bufferedWriter.write(currentOutputLine);
            bufferedWriter.newLine();
        }

        bufferedWriter.write("\nArgument List Array:\n\n");

        for(int i = 0; i < ala1.size(); i++) {
            String currentOutputLine = ala1.get(i).name + " " + "#" + ala1.get(i).position + " " + ala1.get(i).param;

            bufferedWriter.write(currentOutputLine);
            bufferedWriter.newLine();
        }

        bufferedWriter.write("\nIntermediate Code:\n\n");

        for(int i = 0; i < intermediateFile.size(); i++) {

            bufferedWriter.write(intermediateFile.get(i));
            bufferedWriter.newLine();
        }

        bufferedWriter.write("\nPass Two:\n\nUpdated Argument List Array:\n\n");

        for(int k = 0; k < intermediateFile.size(); k++) {
            for(int j = 0; j < mnt.size(); j++) {
                String currentIntermediateMacro[] = intermediateFile.get(k).split(" ");
                if(currentIntermediateMacro[0].equals(mnt.get(j).name)) {
                    String currentArgs[] = currentIntermediateMacro[1].split(",");

                    for(int i = 0; i < mnt.get(j).paramCount; i++) {
                        String currentOutputLine = mnt.get(j).name + " " + "#" + (i + 1) + " " + currentArgs[i];

                        bufferedWriter.write(currentOutputLine);
                        bufferedWriter.newLine();
                    }
                }
            }
        }

        bufferedWriter.write("\nExpanded Code:\n\n");

        for(int k = 0; k < intermediateFile.size(); k++) {
            for(int j = 0; j < mnt.size(); j++) {
                String currentIntermediateMacro[] = intermediateFile.get(k).split(" ");

                if(currentIntermediateMacro[0].equals(mnt.get(j).name)) {
                    for(int i = mnt.get(j).mdtc; i < mnt.get(j).paramCount; i++) {
                        String currentMdtDef[] = mdt.get(i).definition.split(" ");
                        String currentOutputLine = currentMdtDef[0];

                        bufferedWriter.write(currentOutputLine);
                        bufferedWriter.newLine();
                    }
                    
                    continue;
                }

                bufferedWriter.write(intermediateFile.get(k));
                bufferedWriter.newLine();
            }
        }

        bufferedWriter.close();
    }
}
