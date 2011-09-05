package JavaO.Tables;

/**
 *
 * @author nikita
 */

import java.util.ArrayList;
import java.util.Iterator;
import JavaO.ErrorMessage;

public class Table {
    
    static final int CategoryConst = 1,
                    CategoryVar = 2,
                    CategoryType = 3,
                    CategoryStProc = 4,
                    CategoryModule = 5,
                    CategoryGuard = 6;
    
    static final int TypeInt = 0,
                    TypeBool = 1,
                    TypeNone = 2;
    
    /*private*/ static ArrayList NamesTable = new ArrayList<TableItem>();
    
    /*private*/ static int Top = -1, Bottom = -1, Current = -1;
    
    static void enterName(String Name, int Category, int Type, int Value){
        TableItem Item = new TableItem();
        Item.Name = new String(Name);
        Item.Category = Category;
        Item.Type = Type;
        Item.Val = Value;
        Top++;
        //NamesTable.add(Top, Item);
        NamesTable.add(Item);
    }
    
    static void openScope(){
        enterName("", CategoryGuard, TypeNone, 0);
        if(Top == 0){
            Bottom = Top;
        }
    }
    
    static void closeScope(){
        while(((TableItem)NamesTable.get(Top)).Category != CategoryGuard){
            NamesTable.remove(Top--);
            //Top--;
        }
        NamesTable.remove(Top--);
        //Top--;
        Bottom = Top;
    }
    
    static TableItem NewName(String Name, int Category){
        int ItemKey = Top;
        TableItem Item = (TableItem)NamesTable.get(ItemKey);
        while(Item.Category != CategoryGuard && Item.Name.compareTo(Name) != 0){
            Item = (TableItem)NamesTable.get(--ItemKey);
        }
        if(Item.Category == CategoryGuard){
            Item = new TableItem();
            Item.Name = new String(Name);
            Item.Category = Category;
            Item.Type = TypeInt;
            Item.Val = 0;
            Top++;
            //NamesTable.add(Top, Item);
            NamesTable.add(Item);
        }
        else{
            ErrorMessage.Error("Double name declaration.");
        }
        return Item;
    }
    
    static TableItem findName(String Name){
        int Index = -1, i = 0;
        TableItem Item;
        for(Iterator<TableItem> Iter = NamesTable.iterator(); Iter.hasNext(); i++){
            Item = Iter.next();
            if(Item.Name.equals(Name)){
                Index = i;
                break;
            }
        }
        if(Index == -1){
            ErrorMessage.Error("Undeclared name.");
        }
        return (TableItem)NamesTable.get(Index);
    }
    
    static TableItem FirstVar(){
        Current = Top;
        return NextVar();
    }
    
    static TableItem NextVar(){
        TableItem Item;
        while(Current != Bottom && 
                ((TableItem)NamesTable.get(Current)).Category != CategoryVar){
            Current--;
        }
        
        if(Current == Bottom){
            return null;
        }
        else{
            Item = (TableItem)NamesTable.get(Current);
            Current--;
            return Item;
        }
    }
    
}
