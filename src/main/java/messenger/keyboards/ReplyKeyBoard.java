package messenger.keyboards;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyBoard {

    private ReplyKeyboardMarkup keyboardMarkup;
    private KeyboardRow row;
    private List<KeyboardRow> keyboard;

    public ReplyKeyBoard(){
        keyboardMarkup = new ReplyKeyboardMarkup();
        keyboard = new ArrayList<>();
    }

    public ReplyKeyboardMarkup subscribeChoice(List<String> names){

        for(int i=0;i<names.size();i++){
            row = new KeyboardRow();
            row.add(names.get(i));
            keyboard.add(row);
        }
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
