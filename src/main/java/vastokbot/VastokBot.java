package vastokbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import static java.lang.Math.toIntExact;

public class VastokBot extends TelegramLongPollingBot {
    private static List<String> photoURLs = new ArrayList<>();
    private static BotLogger botLogger = new BotLogger();
    private static Integer check;
    private static String shvulya;

    public String getBotUsername() {
        return "VastokBot";
    }

    public String getBotToken() {
        return "624467793:AAEFYTPdSNn_z3Qi5VnL3hkgCkjKDa3X09M";
    }

    public String getShvulya(String text){
        StringBuilder result = new StringBuilder();
        StringBuilder sb = new StringBuilder(text);
        StringBuilder buffer = new StringBuilder();
        List<String> list = new ArrayList<>();
        sb.codePoints()
                .mapToObj(c -> String.valueOf((char) c).concat(" "))
                .forEach(buffer::append);
        list.add(buffer.toString());
        for (int i = 0; i < sb.length(); i++) {
            StringBuilder r = new StringBuilder(list.get(list.size()-1));
            r.append(r.charAt(0));
            r.append(" ");
            r.deleteCharAt(0);
            r.deleteCharAt(0);
            list.add(r.toString());
        }

        for (String s:list) {
            result.append(s+"\n");
        }
        if(result.toString().length()<4096)
        return result.toString();
        else
            return "Сообщение получилось слишком длинное: " + result.toString().length() + " символов";
    }


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try{
            photoURLs = Files.readAllLines(Paths.get("src/main/java/resources/images.list"), StandardCharsets.UTF_8);
        }
        catch (IOException ignored){
            botLogger.log(Level.WARNING, "Source reading exception", "Exception reading file");
        }

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
                    execute(message);
                    check=0;// Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(update.getMessage().getText().equals("/help")){
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("InlineQuery commands:\nd - Фото с дачи");
                try {
                    execute(message);
                    check=0;// Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(update.getMessage().getText().equals("/shvulya")){
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Введите новую швулю");
                try {
                    execute(message);
                    check = 1;// Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(check==1){
                SendMessage message = new SendMessage().setChatId(chat_id).setText(getShvulya(message_text));
                try {
                    execute(message);
                    check = 0;// Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                SendMessage message = new SendMessage().setChatId(chat_id).setText("Я не смог Вас понять, введите команду");
                try {
                    execute(message);
                    check = 0;// Sending our message object to user
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

            if(query.equals("d")){

            Collections.shuffle(photoURLs);
            List<InlineQueryResult> results = new ArrayList<>();
            for(int i = 1; i<50;i++){
                results.add(new InlineQueryResultPhoto().setId(String.valueOf(i))
                        .setPhotoUrl(photoURLs.get(i))
                        .setThumbUrl(photoURLs.get(i)));
                //
            }

            AnswerInlineQuery answer = new AnswerInlineQuery()
                    .setPersonal(false)
                    .setCacheTime(0);
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
