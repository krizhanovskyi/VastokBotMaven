package vastokbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class VastokBot extends TelegramLongPollingBot {

    public String getBotUsername() {
        return "VastokBot";
    }

    public String getBotToken() {
        return "624467793:AAEFYTPdSNn_z3Qi5VnL3hkgCkjKDa3X09M";
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new VastokBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/start")) {

                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Жигулевич");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText("Жигулевич").setCallbackData("update_msg_text"));
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        } else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("update_msg_text")) {
                String answer = "Васток пидаларас";
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId(toIntExact(message_id))
                        .setText(answer);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(update.hasInlineQuery()){

            String query = update.getInlineQuery().getQuery();

            if(query.equals("get")){

            List<InlineQueryResult> results = new ArrayList<>();

            InlineQueryResultPhoto photo = new InlineQueryResultPhoto();
            photo.setId("1");
            photo.setPhotoUrl("https://picua.org/images/2018/12/30/f07bdb843968ddcb2b7ab18526b5f080.jpg");
            photo.setThumbUrl("https://picua.org/images/2018/12/30/f07bdb843968ddcb2b7ab18526b5f080.jpg");

            InlineQueryResultPhoto photo2 = new InlineQueryResultPhoto();
            photo2.setId("2");
            photo2.setPhotoUrl("https://picua.org/images/2018/12/30/94851f7f03bcd7884f28fc65680175c3.jpg");
            photo2.setThumbUrl("https://picua.org/images/2018/12/30/94851f7f03bcd7884f28fc65680175c3.jpg");

            results.add(photo);
            results.add(photo2);

            AnswerInlineQuery answer = new AnswerInlineQuery();
            InlineQuery inlineQuery = update.getInlineQuery();
            answer.setInlineQueryId(inlineQuery.getId());
            answer.setResults(results);

            try {
                execute(answer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            }
        }
    }
}
