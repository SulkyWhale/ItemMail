package io.github.sulkywhale.itemmail;

import io.github.sulkywhale.itemmail.objects.Mail;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MailManager {

    // A map containing the player receiving the mail and a list containing the mail
    private static final Map<UUID, List<Mail>> mail = new HashMap<>();

    public static List<Mail> getMail(UUID receiver) {
        return mail.computeIfAbsent(receiver, k -> new ArrayList<>());
    }

    public static void addMail(UUID receiver, UUID sender, ItemStack itemStack) {
        getMail(receiver).add(new Mail(sender, itemStack));
    }

    public static void removeMail(UUID receiver, UUID sender, ItemStack itemStack) {
        getMail(receiver).remove(new Mail(sender, itemStack));
    }

    public static void cleanupMail(UUID receiver) {
        if (getMail(receiver).isEmpty()) {
            mail.remove(receiver);
        }
    }

    @Unmodifiable
    public static Map<UUID, List<Mail>> getMailMap() {
        return Collections.unmodifiableMap(mail);
    }
}
