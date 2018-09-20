package jsesh.mdc.lex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdc.constants.SymbolCodes;
import jsesh.mdc.constants.ToggleType;

class MDCLexAux
        implements MDCSymbols, SymbolCodes, Scanner
{
    private final int YY_BUFFER_SIZE = 512;
    private final int YY_F = -1;
    private final int YY_NO_STATE = -1;
    private final int YY_NOT_ACCEPT = 0;
    private final int YY_START = 1;
    private final int YY_END = 2;
    private final int YY_NO_ANCHOR = 4;
    private final int YY_BOL = 65536;
    private final int YY_EOF = 65537;
    boolean philologyAsSigns;
    boolean expectSpace = false;
    boolean justAfterSign = false;
    boolean ignoreStars = false;
    boolean debug;
    private BufferedReader yy_reader;
    private int yy_buffer_index;
    private int yy_buffer_read;
    private int yy_buffer_start;
    private int yy_buffer_end;
    private char[] yy_buffer;
    private int yychar;
    private int yyline;
    private boolean yy_at_bol;
    private int yy_lexical_state;

    public void setDebug(boolean d)
    {
        this.debug = d;
    }

    public boolean getDebug()
    {
        return this.debug;
    }

    public void setPhilologyAsSigns(boolean p)
    {
        this.philologyAsSigns = p;
    }

    public boolean getPhilologyAsSigns()
    {
        return this.philologyAsSigns;
    }

    public void fixExpect(Symbol s)
    {
        this.expectSpace = false;
        this.justAfterSign = false;
        switch (s.sym)
        {
            case 12:
            case 14:
            case 38:
                this.expectSpace = true;
        }
        switch (s.sym)
        {
            case 12:
                this.justAfterSign = true;
        }
    }

    public void reset()
    {
        this.ignoreStars = false;
        this.expectSpace = false;
        this.justAfterSign = false;
        yybegin(0);
    }

    private Symbol handlePhilology(int type, int sub)
    {
        if (getPhilologyAsSigns())
        {
            if (type == 6) {
                return buildMDCSign(sub * 2, yytext());
            }
            return buildMDCSign(sub * 2 + 1, yytext());
        }
        return buildMDCSubType(type, sub);
    }

    private void printDebug(int code)
    {
        if (this.debug) {
            System.err.println("token : " + code + " " + yytext());
        }
    }

    private Symbol buildMDCSymbol(int type)
    {
        printDebug(type);
        return new Symbol(type);
    }

    private Symbol buildMDCSubType(int type, int subType)
    {
        printDebug(type);
        return new Symbol(type, new MDCSubType(subType));
    }

    private Symbol buildMDCIntValuedSymbol(int type, int value)
    {
        printDebug(type);
        return new Symbol(type, new Integer(value));
    }

    private Symbol buildMDCString(int type, String s)
    {
        printDebug(type);
        return new Symbol(type, s);
    }

    private Symbol buildMDCSign(int subtype, String s)
    {
        printDebug(subtype);
        return new Symbol(12, new MDCSign(subtype, s));
    }

    private Symbol buildMDCModifier(String s)
    {
        printDebug(14);
        return new Symbol(14, MDCModifier.buildMDCModifierFromString(s));
    }

    private Symbol buildMDCToggle(ToggleType v)
    {
        printDebug(27);
        return new Symbol(27, v);
    }

    private Symbol buildMDCShading(String t)
    {
        printDebug(23);
        return new Symbol(23, new MDCShading(t));
    }

    private Symbol buildStartOldCartouche(char code, char part)
    {
        printDebug(4);
        return new Symbol(4, new MDCStartOldCartouche(code, part));
    }

    private Symbol buildBeginCartouche(char type, char part)
    {
        printDebug(3);
        return new Symbol(3, new MDCCartouche(type, part - '0'));
    }

    private Symbol buildEndCartouche(char type, char part)
    {
        printDebug(5);
        return new Symbol(5, new MDCCartouche(type, part - '0'));
    }

    private Symbol buildMDCAlphabetictext(char code, String txt)
    {
        printDebug(25);
        return new Symbol(25, new MDCAlphabeticText(code, txt));
    }

    private Symbol buildHRule(char type)
    {
        printDebug(type);

        String s = yytext();
        int commaIndex = s.indexOf(',');
        int startPos = Integer.parseInt(s.substring(2, commaIndex));
        int endPos = Integer.parseInt(s.substring(commaIndex + 1, s.indexOf('}')));
        return new Symbol(21, new MDCHRule(type, startPos, endPos));
    }

    private int extractIntFromLineSkip()
    {
        int skip = 100;
        String s = yytext();
        int pos = s.indexOf('=');
        if (pos != -1) {
            skip = Integer.parseInt(s.substring(pos + 1, s.indexOf('%')));
        }
        return skip;
    }

    public MDCSyntaxError buildError(String msg, String token)
    {
        String res = msg + " line " + this.yyline + " char " + this.yychar + " at token '" + yytext() + "'";
        return new MDCSyntaxError(res, this.yyline, this.yychar, token);
    }

    MDCLexAux(Reader reader)
    {
        this();
        if (null == reader) {
            throw new Error("Error: Bad input stream initializer.");
        }
        this.yy_reader = new BufferedReader(reader);
    }

    MDCLexAux(InputStream instream)
    {
        this();
        if (null == instream) {
            throw new Error("Error: Bad input stream initializer.");
        }
        this.yy_reader = new BufferedReader(new InputStreamReader(instream));
    }

    private MDCLexAux()
    {
        this.yy_buffer = new char['?'];
        this.yy_buffer_read = 0;
        this.yy_buffer_index = 0;
        this.yy_buffer_start = 0;
        this.yy_buffer_end = 0;
        this.yychar = 0;
        this.yyline = 0;
        this.yy_at_bol = true;
        this.yy_lexical_state = 0;
    }

    private boolean yy_eof_done = false;
    private final int YYINITIAL = 0;
    private final int PROPERTIES = 1;
    private final int[] yy_state_dtrans = { 0, 135 };

    private void yybegin(int state)
    {
        this.yy_lexical_state = state;
    }

    private int yy_advance()
            throws IOException
    {
        if (this.yy_buffer_index < this.yy_buffer_read) {
            return this.yy_buffer[(this.yy_buffer_index++)];
        }
        if (0 != this.yy_buffer_start)
        {
            int i = this.yy_buffer_start;
            int j = 0;
            while (i < this.yy_buffer_read)
            {
                this.yy_buffer[j] = this.yy_buffer[i];
                i++;
                j++;
            }
            this.yy_buffer_end -= this.yy_buffer_start;
            this.yy_buffer_start = 0;
            this.yy_buffer_read = j;
            this.yy_buffer_index = j;
            int next_read = this.yy_reader.read(this.yy_buffer, this.yy_buffer_read, this.yy_buffer.length - this.yy_buffer_read);
            if (-1 == next_read) {
                return 65537;
            }
            this.yy_buffer_read += next_read;
        }
        while (this.yy_buffer_index >= this.yy_buffer_read)
        {
            if (this.yy_buffer_index >= this.yy_buffer.length) {
                this.yy_buffer = yy_double(this.yy_buffer);
            }
            int next_read = this.yy_reader.read(this.yy_buffer, this.yy_buffer_read, this.yy_buffer.length - this.yy_buffer_read);
            if (-1 == next_read) {
                return 65537;
            }
            this.yy_buffer_read += next_read;
        }
        return this.yy_buffer[(this.yy_buffer_index++)];
    }

    private void yy_move_end()
    {
        if ((this.yy_buffer_end > this.yy_buffer_start) && ('\n' == this.yy_buffer[(this.yy_buffer_end - 1)])) {
            this.yy_buffer_end -= 1;
        }
        if ((this.yy_buffer_end > this.yy_buffer_start) && ('\r' == this.yy_buffer[(this.yy_buffer_end - 1)])) {
            this.yy_buffer_end -= 1;
        }
    }

    private boolean yy_last_was_cr = false;

    private void yy_mark_start()
    {
        for (int i = this.yy_buffer_start; i < this.yy_buffer_index; i++)
        {
            if (('\n' == this.yy_buffer[i]) && (!this.yy_last_was_cr)) {
                this.yyline += 1;
            }
            if ('\r' == this.yy_buffer[i])
            {
                this.yyline += 1;
                this.yy_last_was_cr = true;
            }
            else
            {
                this.yy_last_was_cr = false;
            }
        }
        this.yychar = (this.yychar + this.yy_buffer_index - this.yy_buffer_start);

        this.yy_buffer_start = this.yy_buffer_index;
    }

    private void yy_mark_end()
    {
        this.yy_buffer_end = this.yy_buffer_index;
    }

    private void yy_to_mark()
    {
        this.yy_buffer_index = this.yy_buffer_end;
        this.yy_at_bol = ((this.yy_buffer_end > this.yy_buffer_start) && (('\r' == this.yy_buffer[(this.yy_buffer_end - 1)]) || ('\n' == this.yy_buffer[(this.yy_buffer_end - 1)]) || ('?' == this.yy_buffer[(this.yy_buffer_end - 1)]) || ('?' == this.yy_buffer[(this.yy_buffer_end - 1)])));
    }

    private String yytext()
    {
        return new String(this.yy_buffer, this.yy_buffer_start, this.yy_buffer_end - this.yy_buffer_start);
    }

    private int yylength()
    {
        return this.yy_buffer_end - this.yy_buffer_start;
    }

    private char[] yy_double(char[] buf)
    {
        char[] newbuf = new char[2 * buf.length];
        for (int i = 0; i < buf.length; i++) {
            newbuf[i] = buf[i];
        }
        return newbuf;
    }

    private final int YY_E_INTERNAL = 0;
    private final int YY_E_MATCH = 1;
    private String[] yy_error_string = { "Error: Internal error.\n", "Error: Unmatched input.\n" };

    private void yy_error(int code, boolean fatal)
    {
        System.out.print(this.yy_error_string[code]);
        System.out.flush();
        if (fatal) {
            throw new Error("Fatal Error.\n");
        }
    }

    private static int[][] unpackFromString(int size1, int size2, String st)
    {
        int colonIndex = -1;

        int sequenceLength = 0;
        int sequenceInteger = 0;

        int[][] res = new int[size1][size2];
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                if (sequenceLength != 0)
                {
                    res[i][j] = sequenceInteger;
                    sequenceLength--;
                }
                else
                {
                    int commaIndex = st.indexOf(',');

                    String workString = commaIndex == -1 ? st : st.substring(0, commaIndex);
                    st = st.substring(commaIndex + 1);
                    colonIndex = workString.indexOf(':');
                    if (colonIndex == -1)
                    {
                        res[i][j] = Integer.parseInt(workString);
                    }
                    else
                    {
                        String lengthString = workString.substring(colonIndex + 1);
                        sequenceLength = Integer.parseInt(lengthString);
                        workString = workString.substring(0, colonIndex);
                        sequenceInteger = Integer.parseInt(workString);
                        res[i][j] = sequenceInteger;
                        sequenceLength--;
                    }
                }
            }
        }
        return res;
    }

    private int[] yy_acpt = { 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 0, 4, 4, 0, 4, 4, 0, 4, 4, 0, 4, 4, 0, 4, 0, 4, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 4, 4, 0, 0, 4, 4, 4, 4, 4 };
    private static int[] yy_cmap = unpackFromString(1, 65538, "20:9,59,29,20:2,29,20:18,33,1,50,32,35,5,51,30,54,53,42,16,13,2,61,62,48,37,38,39,40,4:5,41,20,44,3,49,6,60,56:5,45,56,45,56:3,15,56:2,58,56:2,55,45,56:7,43,18,52,36,31,57,10,34,7,26,9,47,17,64,17:3,8,46,23,22,17,24,11,19,27,25,63,17:3,21,12,28,14,20:65410,0:2")[0];
    private static int[] yy_rmap = unpackFromString(1, 149, "0,1,2,3,1,4,5,6,7,8,4,9,10,11,12,13,1,14,15,16,1,17,18,1:2,4,19,20,21,22,23,24,1:4,25,1:2,26,1,27,1:5,28,1:2,29,1:10,30,1,31,1:2,32,1:13,4,1:3,4,1,33,34,1:4,35,36,37,1,38,39,40,41,42,43,44,45,46,47,48,25,49,50,51,52,53,1,54,55,56,57,58,59,60,61,62,63,1,64,65,66,10,67,68,69,70,71,72,73,74,48,75,76,77,78,79,80,81,82,83,84,85")[0];
    private static int[][] yy_nxt = unpackFromString(86, 65, "1,2,3,4,5,6,7,5:5,8,95,104,5,111,5,9,94,95,141,10,5,148,5:3,11,12,116,12,13,12,5,14,15,103:2,5:2,16,17,18,19,5:2,94,103,20,119,21,122,22,23,5:2,24,25,12,110,26,27,115,118,-1:66,28,93,92,-1:25,93,-1,93,-1,93,-1:25,93,-1:7,29,-1:26,29,-1,29,30,29,-1:25,29,-1:9,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5:7,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1:2,5:2,-1:7,102,-1:61,31,-1,32,-1:30,31:4,-1:7,31,-1:3,33,-1:20,138,-1:3,34,-1:2,143,-1:53,96,-1,38,105:5,-1:3,105,-1,105,-1,105,-1,105:7,-1:6,105,-1:2,96:4,-1:4,105:3,96,-1:6,39,105,-1,105,-1:4,105:2,-1,11,-1,11:15,97,11:11,106,11:34,-1:29,41,-1,41,-1,41,-1:25,41,-1:7,43,-1:6,44,-1:22,45,100,46,-1:2,47,101,108,113,-1:35,48,-1:22,49,-1:66,50,-1:70,52,-1:28,53,-1:5,54,-1:17,55,-1:12,56,-1:6,57,58,-1:2,59,-1:19,60,-1:9,61,-1:14,60,-1:2,62:2,-1:6,63,60,61,62,-1:15,61,-1:51,66,67,-1:64,69,-1:73,70,-1:65,71,-1:4,28,-1:26,28,-1,28,-1,28,-1:25,28,-1:7,29,-1:26,29,-1,29,-1,29,-1:25,29,-1:7,74,-1:6,44,-1:24,46,-1:34,31,-1:32,31:4,-1:7,31,-1:17,36:15,126,36,98,36:46,-1:2,99,-1,99,-1:2,105:5,-1:3,105,-1,105,-1,105,-1,105:7,-1:6,105,-1:2,99:4,-1:4,105:3,99,-1:6,105:2,-1,105,-1:4,105:2,-1:29,128,-1,128,-1,128,-1:25,128,-1:43,101,108,113,-1:60,76,-1:65,77:3,-1:8,77,-1:25,78,-1:24,78,-1:11,78,-1:69,79,-1:17,86,-1:32,86:4,-1:7,86,-1:20,87,-1:2,87:5,-1:3,87,-1,87,-1,87,-1,87:7,-1:3,87,-1:2,87,-1:2,87:4,-1:4,87:4,-1:6,87:2,-1,87,-1:4,87:2,-1:4,117,-1:32,117:4,-1:7,117,-1:18,93,-1:26,93,-1,93,-1,93,-1:25,93,-1:9,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5:7,-1:6,5,-1:2,121:3,5,-1:4,5:3,121,40,-1:5,5:2,-1,5,-1,5,-1:2,5:2,-1:4,96,-1:32,96:4,-1:7,96,-1:17,11:17,97,11:11,106,11:34,-1,36:15,107,36,98,36:46,-1:4,99,-1:32,99:4,-1:7,99,-1:49,100,-1:70,108,113,-1:32,120,-1:60,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5:7,-1:6,5,-1:2,5:4,-1:4,5:4,51,-1:5,5:2,-1,5,-1,5,-1:2,5:2,-1:52,35,-1:16,96,-1:2,105:5,-1:3,105,-1,105,-1,105,-1,105:7,-1:6,105,-1:2,96:4,-1:4,105:3,96,-1:6,105:2,-1,105,-1:4,105:2,-1,11,-1,11:15,112,11:11,106,11:34,-1:40,113,-1:25,109:17,114,109:31,64,109,-1,109:12,-1,124:3,5,124:2,5:5,124:3,5,124,5,124,5,124,5:7,124,-1,124:4,5,124:2,5:4,124:4,5:4,124:6,5:2,124,5,124,5,124:2,5:2,-1:7,36:5,-1:4,36:2,-1,37,-1,36:7,-1:6,36,-1:11,36:2,-1:15,36:2,-1,11:17,97,11:11,137,11:34,-1,109:28,-1,109:35,-1:4,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5:7,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1,72,5:2,-1:52,42,-1:16,117,93,-1:31,117:4,-1:7,117,-1:20,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5:7,-1:6,5,-1:2,121:3,5,-1:4,5:3,121,40,-1:5,5:2,-1,5,-1,5,-1,73,5:2,-1,109:17,114,109:31,64,109,65,109:12,-1:9,130,-1:59,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5:7,-1:6,5,-1:2,5:4,-1:4,5:4,75,-1:5,5:2,-1,5,-1,5,-1:2,5:2,-1:52,68,-1:16,123,-1:8,131,-1:23,123:4,-1:7,123,-1:30,91,-1:51,36:6,-1:5,36:4,-1:2,36,-1,36,-1:7,36:6,-1,36:11,-1:2,36:15,-1:6,5,-1:2,5:2,80,5:2,-1:3,5,-1,5,-1,5,-1,5:7,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1:2,5:2,-1:4,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5:6,84,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1:2,5:2,-1:10,132,-1:58,133,-1:32,133:4,-1:7,133,-1:27,81,-1:57,133,-1:9,82,-1:22,133:4,-1:7,133,-1:20,134,-1:9,83,-1:22,134:4,-1:7,134,-1:16,1,95:2,85,86,95:2,87:5,95,88,125,87,95,87,95,87,95,87:7,95,89,95,87,95,89,87,95:2,86:4,95:4,87:3,86,95:3,90,95:2,87:2,95,87,89,95:3,87:2,-1:4,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5:2,127,5:4,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1:2,5:2,-1:4,123,-1:32,123:4,-1:7,123,-1:20,134,-1:32,134:4,-1:7,134,-1:20,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5:2,129,5:4,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1:2,5:2,-1:4,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5,136,5:5,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1:2,5:2,-1:4,142,-1:8,139,-1:23,142:4,-1:7,142,-1:20,142,-1:32,142:4,-1:7,142,-1:20,5,-1:2,5:3,140,5,-1:3,5,-1,5,-1,5,-1,5:7,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1:2,5:2,-1:4,5,-1:2,5:4,144,-1:3,5,-1,5,-1,5,-1,5:7,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1:2,5:2,-1:4,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5:5,145,5,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1:2,5:2,-1:4,5,-1:2,5:3,146,5,-1:3,5,-1,5,-1,5,-1,5:7,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1:2,5:2,-1:4,5,-1:2,5:5,-1:3,5,-1,5,-1,5,-1,5:4,147,5:2,-1:6,5,-1:2,5:4,-1:4,5:4,-1:6,5:2,-1,5,-1,5,-1:2,5:2");

    public Symbol next_token()
            throws IOException
    {
        int yy_anchor = 4;
        int yy_state = this.yy_state_dtrans[this.yy_lexical_state];
        int yy_next_state = -1;
        int yy_last_accept_state = -1;
        boolean yy_initial = true;

        yy_mark_start();
        int yy_this_accept = this.yy_acpt[yy_state];
        if (0 != yy_this_accept)
        {
            yy_last_accept_state = yy_state;
            yy_mark_end();
        }
        for (;;)
        {
            int yy_lookahead;
            if ((yy_initial) && (this.yy_at_bol)) {
                yy_lookahead = 65536;
            } else {
                yy_lookahead = yy_advance();
            }
            yy_next_state = -1;
            yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
            if ((65537 == yy_lookahead) && (true == yy_initial))
            {
                printDebug(0);
                return buildMDCSymbol(0);
            }
            if (-1 != yy_next_state)
            {
                yy_state = yy_next_state;
                yy_initial = false;
                yy_this_accept = this.yy_acpt[yy_state];
                if (0 != yy_this_accept)
                {
                    yy_last_accept_state = yy_state;
                    yy_mark_end();
                }
            }
            else
            {
                if (-1 == yy_last_accept_state) {
                    throw new Error("Lexical Error: Unmatched Input.");
                }
                yy_anchor = this.yy_acpt[yy_last_accept_state];
                if (0 != (0x2 & yy_anchor)) {
                    yy_move_end();
                }
                yy_to_mark();
                switch (yy_last_accept_state)
                {
                    case -2:
                    case 1:
                        break;
                    case 2:
                        return buildMDCIntValuedSymbol(13, extractIntFromLineSkip());
                    case -3:
                        break;
                    case 3:
                        return buildMDCSymbol(22);
                    case -4:
                        break;
                    case 4:
                        return buildMDCSymbol(11);
                    case -5:
                        break;
                    case 5:
                        return buildMDCSign(9, yytext());
                    case -6:
                        break;
                    case 6:
                        return buildMDCSymbol(19);
                    case -7:
                        break;
                    case 7:
                        return buildMDCToggle(ToggleType.LACUNA);
                    case -8:
                        break;
                    case 8:
                        return buildMDCString(39, yytext());
                    case -9:
                        break;
                    case 9:
                        return buildMDCModifier(yytext());
                    case -10:
                        break;
                    case 10:
                        return buildMDCSign(3, "o");
                    case -11:
                        break;
                    case 11:
                        return buildMDCString(26, yytext().substring(1));
                    case -12:
                        break;
                    case 12:
                        this.justAfterSign = false;
                        if (this.expectSpace) {
                            return buildMDCSymbol(28);
                        }
                    case -13:
                        break;
                    case 13:
                        if (this.justAfterSign) {
                            return buildMDCSymbol(15);
                        }
                        return buildMDCToggle(ToggleType.SHADINGTOGGLE);
                    case -14:
                        break;
                    case 14:
                        return buildMDCToggle(ToggleType.BLACKRED);
                    case -15:
                        break;
                    case 15:
                        return buildMDCToggle(ToggleType.OMMIT);
                    case -16:
                        break;
                    case 16:
                        return buildMDCSymbol(9);
                    case -17:
                        break;
                    case 17:
                        return buildMDCSymbol(24);
                    case -18:
                        break;
                    case 18:
                        yybegin(1);return buildMDCSymbol(30);
                    case -19:
                        break;
                    case 19:
                        return buildStartOldCartouche('c', 'a');
                    case -20:
                        break;
                    case 20:
                        return buildEndCartouche('c', '2');
                    case -21:
                        break;
                    case 21:
                        return buildMDCSymbol(2);
                    case -22:
                        break;
                    case 22:
                        return buildMDCSymbol(10);
                    case -23:
                        break;
                    case 23:
                        return buildMDCSymbol(8);
                    case -24:
                        break;
                    case 24:
                        return buildMDCSign(9, yytext());
                    case -25:
                        break;
                    case 25:
                        return buildMDCSign(4, "O");
                    case -26:
                        break;
                    case 26:
                        return buildMDCSign(1, ".");
                    case -27:
                        break;
                    case 27:
                        return buildMDCSign(8, "/");
                    case -28:
                        break;
                    case 28:
                        return buildMDCSymbol(16);
                    case -29:
                        break;
                    case 29:
                        return buildMDCSymbol(22);
                    case -30:
                        break;
                    case 30:
                        return buildMDCToggle(ToggleType.SHADINGTOGGLE);
                    case -31:
                        break;
                    case 31:
                        return buildMDCIntValuedSymbol(18, Integer.parseInt(yytext().substring(1)));
                    case -32:
                        break;
                    case 32:
                        return buildMDCToggle(ToggleType.LINELACUNA);
                    case -33:
                        break;
                    case 33:
                        return handlePhilology(7, 56);
                    case -34:
                        break;
                    case 34:
                        yybegin(1);return buildMDCSymbol(37);
                    case -35:
                        break;
                    case 35:
                        return handlePhilology(7, 52);
                    case -36:
                        break;
                    case 36:
                        String txt = "";
                        if (yytext().length() > 2) {
                            txt = yytext().substring(2);
                        }
                        return buildMDCAlphabetictext(yytext().charAt(1), txt.replaceAll("\\\\\\+", "+").replaceAll("\\\\\\\\", "\\\\"));
                    case -37:
                        break;
                    case 37:
                        return buildMDCSymbol(17);
                    case -38:
                        break;
                    case 38:
                        return buildMDCModifier(yytext());
                    case -39:
                        break;
                    case 39:
                        return buildMDCModifier(yytext());
                    case -40:
                        break;
                    case 40:
                        return buildEndCartouche(yytext().charAt(0), '2');
                    case -41:
                        break;
                    case 41:
                        this.justAfterSign = false;
                        if (this.expectSpace) {
                            return buildMDCSymbol(29);
                        }
                    case -42:
                        break;
                    case 42:
                        return handlePhilology(7, 54);
                    case -43:
                        break;
                    case 43:
                        System.out.println("affiche " + this.justAfterSign);
                        if (this.justAfterSign) {
                            return buildMDCShading("#1234");
                        }
                        return buildMDCToggle(ToggleType.SHADINGTOGGLE);
                    case -44:
                        break;
                    case 44:
                        return buildMDCToggle(ToggleType.SHADINGOFF);
                    case -45:
                        break;
                    case 45:
                        return buildMDCSymbol(15);
                    case -46:
                        break;
                    case 46:
                        return buildMDCToggle(ToggleType.SHADINGON);
                    case -47:
                        break;
                    case 47:
                        return buildMDCShading(yytext());
                    case -48:
                        break;
                    case 48:
                        return buildMDCToggle(ToggleType.RED);
                    case -49:
                        break;
                    case 49:
                        return buildMDCToggle(ToggleType.BLACK);
                    case -50:
                        break;
                    case 50:
                        return buildMDCSymbol(35);
                    case -51:
                        break;
                    case 51:
                        return buildEndCartouche('c',
                                yytext().charAt(0));
                    case -52:
                        break;
                    case 52:
                        return buildMDCSymbol(34);
                    case -53:
                        break;
                    case 53:
                        return handlePhilology(6, 56);
                    case -54:
                        break;
                    case 54:
                        return handlePhilology(6, 52);
                    case -55:
                        break;
                    case 55:
                        return handlePhilology(6, 54);
                    case -56:
                        break;
                    case 56:
                        return handlePhilology(6, 50);
                    case -57:
                        break;
                    case 57:
                        return handlePhilology(6, 53);
                    case -58:
                        break;
                    case 58:
                        return handlePhilology(6, 51);
                    case -59:
                        break;
                    case 59:
                        return handlePhilology(6, 55);
                    case -60:
                        break;
                    case 60:
                        return buildStartOldCartouche('c', yytext().charAt(1));
                    case -61:
                        break;
                    case 61:
                        return buildBeginCartouche(yytext().charAt(1), '1');
                    case -62:
                        break;
                    case 62:
                        return buildBeginCartouche('c', yytext().charAt(1));
                    case -63:
                        break;
                    case 63:
                        return buildStartOldCartouche(yytext().charAt(1), 'a');
                    case -64:
                        break;
                    case 64:
                        return buildMDCSign(10, yytext());
                    case -65:
                        break;
                    case 65:
                        return handlePhilology(7, 53);
                    case -66:
                        break;
                    case 66:
                        return buildMDCSymbol(34);
                    case -67:
                        break;
                    case 67:
                        return handlePhilology(7, 51);
                    case -68:
                        break;
                    case 68:
                        return handlePhilology(7, 50);
                    case -69:
                        break;
                    case 69:
                        return handlePhilology(7, 55);
                    case -70:
                        break;
                    case 70:
                        return buildMDCSign(2, "..");
                    case -71:
                        break;
                    case 71:
                        return buildMDCSign(5, "//");
                    case -72:
                        break;
                    case 72:
                        return buildMDCSign(6, "v/");
                    case -73:
                        break;
                    case 73:
                        return buildMDCSign(7, "h/");
                    case -74:
                        break;
                    case 74:
                        return buildMDCToggle(ToggleType.SHADINGTOGGLE);
                    case -75:
                        break;
                    case 75:
                        return buildEndCartouche(yytext().charAt(0),
                                yytext().charAt(1));
                    case -76:
                        break;
                    case 76:
                        return buildMDCSymbol(35);
                    case -77:
                        break;
                    case 77:
                        return
                                buildBeginCartouche(yytext().charAt(1),
                                        yytext().charAt(2));
                    case -78:
                        break;
                    case 78:
                        return buildStartOldCartouche(
                                yytext().charAt(1),
                                yytext().charAt(2));
                    case -79:
                        break;
                    case 79:
                        return buildMDCSymbol(36);
                    case -80:
                        break;
                    case 80:
                        return buildMDCSymbol(42);
                    case -81:
                        break;
                    case 81:
                        return buildMDCSymbol(20);
                    case -82:
                        break;
                    case 82:
                        return buildHRule('l');
                    case -83:
                        break;
                    case 83:
                        return buildHRule('L');
                    case -84:
                        break;
                    case 84:
                        return buildMDCSymbol(43);
                    case -85:
                        break;
                    case 85:
                        return buildMDCSymbol(41);
                    case -86:
                        break;
                    case 86:
                        return buildMDCIntValuedSymbol(32, Integer.parseInt(yytext()));
                    case -87:
                        break;
                    case 87:
                        return buildMDCString(40, yytext());
                    case -88:
                        break;
                    case 88:
                        return buildMDCSymbol(33);
                    case -89:
                        break;
                    case -90:
                    case 89:
                        break;
                    case 90:
                        yybegin(0);return buildMDCSymbol(31);
                    case -91:
                        break;
                    case 91:
                        yybegin(0);return buildMDCSymbol(38);
                    case -92:
                        break;
                    case 93:
                        return buildMDCIntValuedSymbol(13, extractIntFromLineSkip());
                    case -93:
                        break;
                    case 94:
                        return buildMDCSign(9, yytext());
                    case -94:
                        break;
                    case 95:
                        return buildMDCString(39, yytext());
                    case -95:
                        break;
                    case 96:
                        return buildMDCModifier(yytext());
                    case -96:
                        break;
                    case 97:
                        return buildMDCString(26, yytext().substring(1));
                    case -97:
                        break;
                    case 98:
                        txt = "";
                        if (yytext().length() > 2) {
                            txt = yytext().substring(2);
                        }
                        return buildMDCAlphabetictext(yytext().charAt(1), txt.replaceAll("\\\\\\+", "+").replaceAll("\\\\\\\\", "\\\\"));
                    case -98:
                        break;
                    case 99:
                        return buildMDCModifier(yytext());
                    case -99:
                        break;
                    case 100:
                        System.out.println("affiche " + this.justAfterSign);
                        if (this.justAfterSign) {
                            return buildMDCShading("#1234");
                        }
                        return buildMDCToggle(ToggleType.SHADINGTOGGLE);
                    case -100:
                        break;
                    case 101:
                        return buildMDCShading(yytext());
                    case -101:
                        break;
                    case 103:
                        return buildMDCSign(9, yytext());
                    case -102:
                        break;
                    case 104:
                        return buildMDCString(39, yytext());
                    case -103:
                        break;
                    case 105:
                        return buildMDCModifier(yytext());
                    case -104:
                        break;
                    case 106:
                        return buildMDCString(26, yytext().substring(1));
                    case -105:
                        break;
                    case 107:
                        txt = "";
                        if (yytext().length() > 2) {
                            txt = yytext().substring(2);
                        }
                        return buildMDCAlphabetictext(yytext().charAt(1), txt.replaceAll("\\\\\\+", "+").replaceAll("\\\\\\\\", "\\\\"));
                    case -106:
                        break;
                    case 108:
                        return buildMDCShading(yytext());
                    case -107:
                        break;
                    case 110:
                        return buildMDCSign(9, yytext());
                    case -108:
                        break;
                    case 111:
                        return buildMDCString(39, yytext());
                    case -109:
                        break;
                    case 112:
                        return buildMDCString(26, yytext().substring(1));
                    case -110:
                        break;
                    case 113:
                        return buildMDCShading(yytext());
                    case -111:
                        break;
                    case 115:
                        return buildMDCSign(9, yytext());
                    case -112:
                        break;
                    case 116:
                        return buildMDCString(39, yytext());
                    case -113:
                        break;
                    case 118:
                        return buildMDCSign(9, yytext());
                    case -114:
                        break;
                    case 119:
                        return buildMDCString(39, yytext());
                    case -115:
                        break;
                    case 121:
                        return buildMDCSign(9, yytext());
                    case -116:
                        break;
                    case 122:
                        return buildMDCString(39, yytext());
                    case -117:
                        break;
                    case 124:
                        return buildMDCSign(9, yytext());
                    case -118:
                        break;
                    case 125:
                        return buildMDCString(39, yytext());
                    case -119:
                        break;
                    case 127:
                        return buildMDCSign(9, yytext());
                    case -120:
                        break;
                    case 129:
                        return buildMDCSign(9, yytext());
                    case -121:
                        break;
                    case 136:
                        return buildMDCSign(9, yytext());
                    case -122:
                        break;
                    case 137:
                        return buildMDCString(26, yytext().substring(1));
                    case -123:
                        break;
                    case 140:
                        return buildMDCSign(9, yytext());
                    case -124:
                        break;
                    case 141:
                        return buildMDCSign(9, yytext());
                    case -125:
                        break;
                    case 144:
                        return buildMDCSign(9, yytext());
                    case -126:
                        break;
                    case 145:
                        return buildMDCSign(9, yytext());
                    case -127:
                        break;
                    case 146:
                        return buildMDCSign(9, yytext());
                    case -128:
                        break;
                    case 147:
                        return buildMDCSign(9, yytext());
                    case -129:
                        break;
                    case 148:
                        return buildMDCSign(9, yytext());
                    case -130:
                        break;
                    case 0:
                    case 92:
                    case 102:
                    case 109:
                    case 114:
                    case 117:
                    case 120:
                    case 123:
                    case 126:
                    case 128:
                    case 130:
                    case 131:
                    case 132:
                    case 133:
                    case 134:
                    case 135:
                    case 138:
                    case 139:
                    case 142:
                    case 143:
                    default:
                        yy_error(0, false);
                }
                yy_initial = true;
                yy_state = this.yy_state_dtrans[this.yy_lexical_state];
                yy_next_state = -1;
                yy_last_accept_state = -1;
                yy_mark_start();
                yy_this_accept = this.yy_acpt[yy_state];
                if (0 != yy_this_accept)
                {
                    yy_last_accept_state = yy_state;
                    yy_mark_end();
                }
            }
        }
    }
}
