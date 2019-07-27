package jsesh.android.app;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import jsesh.mdc.constants.JSeshInfoConstants;
import jsesh.mdc.file.MDCDocument;
import jsesh.mdc.jseshInfo.JSeshInfoReader;
import jsesh.mdc.model.TopItemList;
import jsesh.mdcDisplayer.preferences.DrawingSpecification;
import jsesh.mdcDisplayer.preferences.DrawingSpecificationsImplementation;

public class ModelPreferences {

    public static void setAsModel(Context context, DrawingSpecification specification) {
        try {
            FileOutputStream fos = context.openFileOutput("modelPreferences.txt", 0);
            BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(fos));

            MDCDocument document = new MDCDocument(new TopItemList(), specification);
            writeHeader(document, bos);

            bos.flush();
            bos.close();
        } catch (IOException e) {
            //TODO Error?
        }
    }

    public static void useModelPreferences(Context context, DrawingSpecification specification) {
        try {
            FileInputStream fis = context.openFileInput("modelPreferences.txt");
            BufferedReader bis = new BufferedReader(new InputStreamReader(fis));
            StringBuffer buffer = new StringBuffer();
            String s;
            while ((s = bis.readLine()) != null) buffer.append(s).append("\n");

            MDCDocument document = new MDCDocument(new TopItemList(), new DrawingSpecificationsImplementation());
            readHeader(document, buffer);
            specification.applyDocumentPreferences(document.getDocumentPreferences());

            bis.close();
        } catch (FileNotFoundException e) {
            MDCDocument document = new MDCDocument(new TopItemList(), new DrawingSpecificationsImplementation());
            specification.applyDocumentPreferences(document.getDocumentPreferences());
        } catch (IOException e) {
            //TODO Error?
        }
    }

    //BELOW MODIFIED FROM MDCDocument and MDCDocumentReader

    private static void writeHeader(MDCDocument document, Writer f) throws IOException {
        writeEntry(f, JSeshInfoConstants.JSESH_INFO, "1.0");
        Map<String, String> map = document.getDocumentPreferences()
                .getStringRepresentation();
        for (String key : map.keySet()) {
            writeEntry(f, key, map.get(key));
        }
    }

    private static void writeEntry(Writer f, String propertyName, String value)
            throws IOException {
        f.write("++" + propertyName);
        if (value != null)
            f.write(" " + value);
        f.write(" +s\n");
    }

    private static void readHeader(MDCDocument document, StringBuffer buff) {
        JSeshInfoReader infoReader = new JSeshInfoReader();
        infoReader.process(buff, document);
    }


}
