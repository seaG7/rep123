package ru.mirea.samsonova.cloudid.data.network;

/**
 * Замоканный JSON карточек родов. Форма ответа близка к Wikipedia REST summary:
 * title, extract, thumbnail. Картинки — те же кадры Wikimedia, что в прототипе,
 * лежат в assets и не сжимаются до превью 330 px. Позже тело заменяется на HTTP.
 */
public class NetworkApi {
    public String getCatalogJson() {
        return "["
                + item("Cu", "Кучевые", "Cumulus",
                "file:///android_asset/clouds/Cu.jpg",
                "Плотные облака с плоским основанием, похожи на хлопья ваты.")
                + ","
                + item("Ci", "Перистые", "Cirrus",
                "file:///android_asset/clouds/Ci.jpg",
                "Высокие тонкие облака из ледяных кристаллов.")
                + ","
                + item("Cb", "Кучево-дождевые", "Cumulonimbus",
                "file:///android_asset/clouds/Cb.jpg",
                "Мощные вертикальные облака, гроза и ливень.")
                + ","
                + item("Sc", "Слоисто-кучевые", "Stratocumulus",
                "file:///android_asset/clouds/Sc.jpg",
                "Низкие серые гряды и волны облаков.")
                + ","
                + item("St", "Слоистые", "Stratus",
                "file:///android_asset/clouds/St.jpg",
                "Серый однородный слой низких облаков.")
                + ","
                + item("Ac", "Высококучевые", "Altocumulus",
                "file:///android_asset/clouds/Ac.jpg",
                "Белые или серые волны в средней тропосфере.")
                + ","
                + item("As", "Высокослоистые", "Altostratus",
                "file:///android_asset/clouds/As.jpg",
                "Молочно-серый слой, солнце просвечивает слабо.")
                + ","
                + item("Ns", "Слоисто-дождевые", "Nimbostratus",
                "file:///android_asset/clouds/Ns.jpg",
                "Тёмный сплошной слой, обложной дождь или снег.")
                + ","
                + item("Cc", "Перисто-кучевые", "Cirrocumulus",
                "file:///android_asset/clouds/Cc.jpg",
                "Мелкие белые хлопья высоко в небе.")
                + ","
                + item("Cs", "Перисто-слоистые", "Cirrostratus",
                "file:///android_asset/clouds/Cs.jpg",
                "Белёсая вуаль, вокруг солнца бывает гало.")
                + ","
                + item("Ct", "Инверсионный след", "Contrail",
                "file:///android_asset/clouds/Ct.jpg",
                "Искусственное облако за самолётом.")
                + "]";
    }

    private static String item(String code, String name, String latin, String image, String extract) {
        return "{\"code\":\"" + code + "\""
                + ",\"name\":\"" + name + "\""
                + ",\"latin\":\"" + latin + "\""
                + ",\"image\":\"" + image + "\""
                + ",\"extract\":\"" + extract + "\"}";
    }
}
