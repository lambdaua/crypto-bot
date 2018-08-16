package messenger.keyboards;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyBoard {
    private InlineKeyboardMarkup markupInline;
    private List<List<InlineKeyboardButton>> rowsInline;
    private List<InlineKeyboardButton> rowInline;

    public InlineKeyBoard(){
        markupInline = new InlineKeyboardMarkup();
        rowsInline = new ArrayList<>();
        rowInline = new ArrayList<>();
    }

    public InlineKeyboardMarkup diffChoice(List<String> callbacks){
        rowInline.add(new InlineKeyboardButton().setText(callbacks.get(0)).setCallbackData(callbacks.get(0)));
        rowInline.add(new InlineKeyboardButton().setText(callbacks.get(1)).setCallbackData(callbacks.get(1)));
        rowInline.add(new InlineKeyboardButton().setText(callbacks.get(2)).setCallbackData(callbacks.get(2)));
        rowInline.add(new InlineKeyboardButton().setText(callbacks.get(3)).setCallbackData(callbacks.get(3)));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public InlineKeyboardMarkup unsubscribeChoice(List<String> callbacks){

        rowInline.add(new InlineKeyboardButton().setText("Yes").setCallbackData(callbacks.get(0)));
        rowInline.add(new InlineKeyboardButton().setText("No").setCallbackData(callbacks.get(1)));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
