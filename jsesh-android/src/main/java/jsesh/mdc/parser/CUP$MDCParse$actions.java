package jsesh.mdc.parser;

import java.util.Stack;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdc.ParserErrorManager;
import jsesh.mdc.constants.ToggleType;
import jsesh.mdc.interfaces.AbsoluteGroupInterface;
import jsesh.mdc.interfaces.BasicItemListInterface;
import jsesh.mdc.interfaces.CadratInterface;
import jsesh.mdc.interfaces.CartoucheInterface;
import jsesh.mdc.interfaces.ComplexLigatureInterface;
import jsesh.mdc.interfaces.HBoxInterface;
import jsesh.mdc.interfaces.HieroglyphInterface;
import jsesh.mdc.interfaces.HorizontalListElementInterface;
import jsesh.mdc.interfaces.InnerGroupInterface;
import jsesh.mdc.interfaces.LigatureInterface;
import jsesh.mdc.interfaces.MDCBuilder;
import jsesh.mdc.interfaces.MDCFileInterface;
import jsesh.mdc.interfaces.ModifierListInterface;
import jsesh.mdc.interfaces.OptionListInterface;
import jsesh.mdc.interfaces.PhilologyInterface;
import jsesh.mdc.interfaces.SubCadratInterface;
import jsesh.mdc.interfaces.TopItemListInterface;
import jsesh.mdc.interfaces.VBoxInterface;
import jsesh.mdc.interfaces.ZoneStartInterface;
import jsesh.mdc.lex.MDCAlphabeticText;
import jsesh.mdc.lex.MDCCartouche;
import jsesh.mdc.lex.MDCHRule;
import jsesh.mdc.lex.MDCModifier;
import jsesh.mdc.lex.MDCShading;
import jsesh.mdc.lex.MDCSign;
import jsesh.mdc.lex.MDCStartOldCartouche;
import jsesh.mdc.lex.MDCSubType;

class CUP$MDCParse$actions
{
    static final int[] defaultPos = { 0, 0, 100 };
    private final MDCParse parser;

    public MDCBuilder getBuilder()
    {
        return this.parser.getBuilder();
    }

    private ParserErrorManager getErrorManager()
    {
        return this.parser.getErrorManager();
    }

    public void doError(String message)
            throws MDCSyntaxError
    {
        throw getErrorManager().buildError(message);
    }

    CUP$MDCParse$actions(MDCParse parser)
    {
        this.parser = parser;
    }

    public final Symbol CUP$MDCParse$do_action(int CUP$MDCParse$act_num, lr_parser CUP$MDCParse$parser, Stack CUP$MDCParse$stack, int CUP$MDCParse$top)
            throws Exception
    {
        switch (CUP$MDCParse$act_num)
        {
            case 73: {
                CartoucheInterface RESULT = null;
                int c1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left;
                int c1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).right;
                MDCStartOldCartouche c1 = (MDCStartOldCartouche)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).value;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                BasicItemListInterface e = (BasicItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int c2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int c2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                MDCCartouche c2 = (MDCCartouche)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                int type = c1.getCartoucheType();
                int leftPart;
                int rightPart;
                switch (c1.getPart())
                {
                    case 'b':
                        leftPart = 1;
                        rightPart = 0;
                        break;
                    case 'm':
                        leftPart = 0;
                        rightPart = 0;
                        break;
                    case 'e':
                        leftPart = 0;
                        rightPart = 2;
                        break;
                    case 'a':
                    default:
                        leftPart = 1;
                        rightPart = 2;
                }
                RESULT = getBuilder().buildCartouche(type, leftPart, e, rightPart);

                Symbol CUP$MDCParse$result = new Symbol(3, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 72: {
                CartoucheInterface RESULT = null;
                int c1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).left;
                int c1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).right;
                MDCCartouche c1 = (MDCCartouche)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).value;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).right;
                BasicItemListInterface e = (BasicItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).value;
                int c2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left;
                int c2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).right;
                MDCCartouche c2 = (MDCCartouche)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).value;
                int mleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int mright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                ModifierListInterface m = (ModifierListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildCartouche(c1.getCartoucheType(), c1.getPart(), e, c2.getPart());

                Symbol CUP$MDCParse$result = new Symbol(3, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 71: {
                OptionListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).right;
                OptionListInterface e1 = (OptionListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                String e2 = (String)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e3left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e3right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                Integer e3 = (Integer)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addOption(e1, e2, e3.intValue());

                Symbol CUP$MDCParse$result = new Symbol(7, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 70: {
                OptionListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).right;
                OptionListInterface e1 = (OptionListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                String e2 = (String)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e3left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e3right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                String e3 = (String)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addOption(e1, e2, e3);

                Symbol CUP$MDCParse$result = new Symbol(7, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 69: {
                OptionListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                OptionListInterface e1 = (OptionListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                String e2 = (String)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addOption(e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(7, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 68: {
                OptionListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                String e1 = (String)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                Integer e2 = (Integer)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildOptionList();
                getBuilder().addOption(RESULT, e1, e2.intValue());

                Symbol CUP$MDCParse$result = new Symbol(7, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 67: {
                OptionListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                String e1 = (String)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                String e2 = (String)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildOptionList();
                getBuilder().addOption(RESULT, e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(7, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 66: {
                OptionListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                String e1 = (String)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildOptionList();
                getBuilder().addOption(RESULT, e1);

                Symbol CUP$MDCParse$result = new Symbol(7, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 65: {
                OptionListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).right;
                OptionListInterface e1 = (OptionListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).value;

                RESULT = e1;

                Symbol CUP$MDCParse$result = new Symbol(6, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 64: {
                ZoneStartInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                OptionListInterface e1 = (OptionListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildZone();
                getBuilder().setOptionList(RESULT, e1);

                Symbol CUP$MDCParse$result = new Symbol(5, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 63: {
                ZoneStartInterface RESULT = null;

                RESULT = getBuilder().buildZone();

                Symbol CUP$MDCParse$result = new Symbol(5, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 62: {
                ModifierListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).right;
                ModifierListInterface e1 = (ModifierListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                MDCModifier e2 = (MDCModifier)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;

                getBuilder().addModifierToModifierList(e1, e2.getName(), e2.getIntValue());

                Symbol CUP$MDCParse$result = new Symbol(20, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 61: {
                ModifierListInterface RESULT = null;

                RESULT = getBuilder().buildModifierList();

                Symbol CUP$MDCParse$result = new Symbol(20, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 60: {
                Boolean RESULT = null;

                RESULT = new Boolean(true);

                Symbol CUP$MDCParse$result = new Symbol(21, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 59: {
                Boolean RESULT = null;

                RESULT = new Boolean(false);

                Symbol CUP$MDCParse$result = new Symbol(21, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 58: {
                int[] RESULT = null;
                int i1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 5)).left;
                int i1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 5)).right;
                Integer i1 = (Integer)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 5)).value;
                int i2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left;
                int i2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).right;
                Integer i2 = (Integer)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).value;
                int i3left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left;
                int i3right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).right;
                Integer i3 = (Integer)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).value;

                int[] t = new int[3];
                t[0] = i1.intValue();
                t[1] = i2.intValue();
                t[2] = i3.intValue();
                RESULT = t;

                Symbol CUP$MDCParse$result = new Symbol(25, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 6)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 57: {
                int[] RESULT = null;

                RESULT = defaultPos;

                Symbol CUP$MDCParse$result = new Symbol(25, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 56: {
                Integer RESULT = null;

                RESULT = new Integer(2);

                Symbol CUP$MDCParse$result = new Symbol(24, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 55: {
                Integer RESULT = null;

                RESULT = new Integer(1);

                Symbol CUP$MDCParse$result = new Symbol(24, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 54: {
                Integer RESULT = null;

                RESULT = new Integer(0);

                Symbol CUP$MDCParse$result = new Symbol(24, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 53: {
                HieroglyphInterface RESULT = null;
                int gleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).left;
                int gright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).right;
                Boolean g = (Boolean)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).value;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).right;
                MDCSign e = (MDCSign)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).value;
                int mleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int mright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                ModifierListInterface m = (ModifierListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int posleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left;
                int posright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).right;
                int[] pos = (int[])((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).value;
                int fleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int fright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                Integer f = (Integer)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                HieroglyphInterface h = getBuilder().buildHieroglyph(g.booleanValue(), e.getType(), e.getString(), m, f.intValue());
                if ((h != null) && (pos != defaultPos)) {
                    getBuilder().setHieroglyphPosition(h, pos[0], pos[1], pos[2]);
                }
                RESULT = h;

                Symbol CUP$MDCParse$result = new Symbol(16, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 52: {
                AbsoluteGroupInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HieroglyphInterface e = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildAbsoluteGroup();
                getBuilder().addHieroglyphToAbsoluteGroup(RESULT, e);

                Symbol CUP$MDCParse$result = new Symbol(27, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 51: {
                AbsoluteGroupInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                AbsoluteGroupInterface e1 = (AbsoluteGroupInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HieroglyphInterface e2 = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addHieroglyphToAbsoluteGroup(e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(27, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 50: {
                AbsoluteGroupInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                AbsoluteGroupInterface e1 = (AbsoluteGroupInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HieroglyphInterface e2 = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addHieroglyphToAbsoluteGroup(e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(26, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 49: {
                LigatureInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HieroglyphInterface e = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildLigature();
                getBuilder().addToLigature(RESULT, e);

                Symbol CUP$MDCParse$result = new Symbol(18, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 48: {
                LigatureInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                LigatureInterface e1 = (LigatureInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HieroglyphInterface e2 = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addToLigature(e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(18, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 47: {
                LigatureInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                LigatureInterface e1 = (LigatureInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HieroglyphInterface e2 = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;

                getBuilder().addToLigature(e1, e2);
                getBuilder().completeLigature(e1);

                Symbol CUP$MDCParse$result = new Symbol(17, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 46: {
                InnerGroupInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                HieroglyphInterface e1 = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HieroglyphInterface e2 = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildOverwrite(e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(13, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 45: {
                Object RESULT = null;

                Symbol CUP$MDCParse$result = new Symbol(22, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 44: {
                Object RESULT = null;

                Symbol CUP$MDCParse$result = new Symbol(22, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 43: {
                BasicItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                BasicItemListInterface e1 = (BasicItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                ToggleType e2 = (ToggleType)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;

                getBuilder().addToggleToBasicItemList(e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(19, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 42: {
                BasicItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                BasicItemListInterface e1 = (BasicItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;

                RESULT = e1;

                getBuilder().addStartHieroglyphicTextToBasicItemList(e1);

                Symbol CUP$MDCParse$result = new Symbol(19, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 41: {
                BasicItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                BasicItemListInterface e1 = (BasicItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                MDCAlphabeticText e2 = (MDCAlphabeticText)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;

                getBuilder().addTextToBasicItemList(e1, e2.getScriptCode(), e2.getText());

                Symbol CUP$MDCParse$result = new Symbol(19, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 40: {
                BasicItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).right;
                BasicItemListInterface e1 = (BasicItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).right;
                CadratInterface e2 = (CadratInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).value;
                int sleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int sright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                Integer s = (Integer)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;

                getBuilder().addCadratToBasicItemList(e1, e2, s.intValue());

                Symbol CUP$MDCParse$result = new Symbol(19, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 39: {
                BasicItemListInterface RESULT = null;

                RESULT = getBuilder().buildBasicItemList();

                Symbol CUP$MDCParse$result = new Symbol(19, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 38: {
                SubCadratInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                BasicItemListInterface e = (BasicItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;

                RESULT = getBuilder().buildSubCadrat(e);

                Symbol CUP$MDCParse$result = new Symbol(15, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 37: {
                PhilologyInterface RESULT = null;
                int p1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left;
                int p1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).right;
                MDCSubType p1 = (MDCSubType)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).value;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                BasicItemListInterface e = (BasicItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int p2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int p2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                MDCSubType p2 = (MDCSubType)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildPhilology(p1.getSubType(), e, p2.getSubType());

                Symbol CUP$MDCParse$result = new Symbol(14, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 36: {
                InnerGroupInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                AbsoluteGroupInterface e = (AbsoluteGroupInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e;

                Symbol CUP$MDCParse$result = new Symbol(11, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 35: {
                InnerGroupInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                SubCadratInterface e = (SubCadratInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e;

                Symbol CUP$MDCParse$result = new Symbol(11, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 34: {
                InnerGroupInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                PhilologyInterface e = (PhilologyInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e;

                Symbol CUP$MDCParse$result = new Symbol(11, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 33: {
                InnerGroupInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                InnerGroupInterface e = (InnerGroupInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e;

                Symbol CUP$MDCParse$result = new Symbol(11, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 32: {
                InnerGroupInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HieroglyphInterface e = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e;

                Symbol CUP$MDCParse$result = new Symbol(11, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 31: {
                InnerGroupInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                LigatureInterface e = (LigatureInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e;

                Symbol CUP$MDCParse$result = new Symbol(11, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 30: {
                ComplexLigatureInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                HieroglyphInterface e1 = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                InnerGroupInterface e2 = (InnerGroupInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildComplexLigature(null, e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(12, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 29: {
                ComplexLigatureInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                InnerGroupInterface e1 = (InnerGroupInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HieroglyphInterface e2 = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildComplexLigature(e1, e2, null);

                Symbol CUP$MDCParse$result = new Symbol(12, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 28: {
                ComplexLigatureInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).right;
                InnerGroupInterface e1 = (InnerGroupInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                HieroglyphInterface e2 = (HieroglyphInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e3left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e3right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                InnerGroupInterface e3 = (InnerGroupInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildComplexLigature(e1, e2, e3);

                Symbol CUP$MDCParse$result = new Symbol(12, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 27: {
                HorizontalListElementInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                CartoucheInterface e = (CartoucheInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e;

                Symbol CUP$MDCParse$result = new Symbol(10, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 26: {
                HorizontalListElementInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                InnerGroupInterface e = (InnerGroupInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e;

                Symbol CUP$MDCParse$result = new Symbol(10, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 25: {
                HorizontalListElementInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                ComplexLigatureInterface e = (ComplexLigatureInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e;

                Symbol CUP$MDCParse$result = new Symbol(10, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 24: {
                HBoxInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                HBoxInterface e1 = (HBoxInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HorizontalListElementInterface e2 = (HorizontalListElementInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;

                getBuilder().addToHorizontalList(e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(9, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 23: {
                HBoxInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HorizontalListElementInterface e = (HorizontalListElementInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildHBox();

                getBuilder().addToHorizontalList(RESULT, e);

                Symbol CUP$MDCParse$result = new Symbol(9, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 22: {
                VBoxInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                VBoxInterface e1 = (VBoxInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HBoxInterface e2 = (HBoxInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;

                getBuilder().addToVerticalList(e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(8, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 21: {
                VBoxInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                HBoxInterface e = (HBoxInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildVBox();
                getBuilder().addToVerticalList(RESULT, e);

                Symbol CUP$MDCParse$result = new Symbol(8, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 20: {
                CadratInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                VBoxInterface e = (VBoxInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                OptionListInterface e1 = (OptionListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildCadrat(e);
                getBuilder().setOptionList(RESULT, e1);

                Symbol CUP$MDCParse$result = new Symbol(4, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 4)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 19: {
                CadratInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).right;
                VBoxInterface e = (VBoxInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).value;

                RESULT = getBuilder().buildCadrat(e);

                Symbol CUP$MDCParse$result = new Symbol(4, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 18: {
                CadratInterface RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                VBoxInterface e = (VBoxInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = getBuilder().buildCadrat(e);

                Symbol CUP$MDCParse$result = new Symbol(4, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 17: {
                CadratInterface RESULT = null;

                doError("unexpected or unknown item.");
                RESULT = null;

                Symbol CUP$MDCParse$result = new Symbol(4, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 16: {
                Integer RESULT = null;
                int eleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int eright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                MDCShading e = (MDCShading)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = new Integer(e.getShading());

                Symbol CUP$MDCParse$result = new Symbol(23, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 15: {
                Integer RESULT = null;

                RESULT = new Integer(0);

                Symbol CUP$MDCParse$result = new Symbol(23, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 14: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                ZoneStartInterface e2 = (ZoneStartInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addZoneStartToTopItemList(e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 13: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                MDCHRule e2 = (MDCHRule)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addHRuleToTopItemList(e1, e2.getLineType(), e2.getStartPos(), e2.getEndPos());

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 12: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;

                RESULT = e1;
                getBuilder().addTabbingClearToTopItemList(e1);

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 11: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).value;
                int e3left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e3right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                OptionListInterface e3 = (OptionListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addTabbingToTopItemList(e1, e3);

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 10: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                Integer e2 = (Integer)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addTabStopToTopItemList(e1, e2.intValue());

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 9: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                Object e2 = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addPageBreakToTopItemList(e1);

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 8: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                Integer e2 = (Integer)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addLineBreakToTopItemList(e1, e2.intValue());

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 7: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                String e2 = (String)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addTextSuperscriptToTopItemList(e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 6: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                MDCAlphabeticText e2 = (MDCAlphabeticText)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addTextToTopItemList(e1, e2.getScriptCode(), e2.getText());

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 5: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;

                RESULT = e1;
                getBuilder().addStartHieroglyphicTextToTopItemList(e1);

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 4: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                ToggleType e2 = (ToggleType)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addToggleToTopItemList(e1, e2);

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 2)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 3: {
                TopItemListInterface RESULT = null;
                int e1left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left;
                int e1right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).right;
                TopItemListInterface e1 = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).value;
                int e2left = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left;
                int e2right = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).right;
                CadratInterface e2 = (CadratInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).value;
                int sleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).left;
                int sright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right;
                Integer s = (Integer)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).value;

                RESULT = e1;
                getBuilder().addCadratToTopItemList(e1, e2, s.intValue());

                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 3)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 2: {
                TopItemListInterface RESULT = null;
                RESULT = getBuilder().buildTopItemList();
                Symbol CUP$MDCParse$result = new Symbol(2, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
            case 1: {
                Object RESULT = null;
                int start_valleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left;
                int start_valright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).right;
                MDCFileInterface start_val = (MDCFileInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).value;
                RESULT = start_val;
                Symbol CUP$MDCParse$result = new Symbol(0, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                CUP$MDCParse$parser.done_parsing();
                return CUP$MDCParse$result;
            }
            case 0: {
                MDCFileInterface RESULT = null;
                int lleft = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left;
                int lright = ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).right;
                TopItemListInterface l = (TopItemListInterface)((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).value;

                RESULT = getBuilder().buildMDCFileInterface(l);
                Symbol CUP$MDCParse$result = new Symbol(1, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 1)).left, ((Symbol)CUP$MDCParse$stack.elementAt(CUP$MDCParse$top - 0)).right, RESULT);

                return CUP$MDCParse$result;
            }
        }
        throw new Exception("Invalid action number found in internal parse table");
    }
}
