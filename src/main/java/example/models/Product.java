package example.models;

import javax.validation.constraints.*;

public class Product {

    //Так как это интернет магазин, то подразумевается, что имя каждого товара уникально

    @NotEmpty(message = "Неверный тип")
    private String type;

    @NotEmpty(message = "Название не может быть пустым")
    private String name;

    @Size(min = 5, max = 5000, message = "Описание может быть в длину от 5 до 5000 символов")
    @NotEmpty(message = "Описание не может быть пустым")
    private String description;

    @DecimalMin(value = "0", message = "Количество не может быть отрицательным")
    @NotNull(message = "Количество не может быть пустым")
    private int countInMoscow;

    @DecimalMin(value = "0", message = "Количество не может быть отрицательным")
    @NotNull(message = "Количество не может быть пустым")
    private int countInKazan;

    @DecimalMin(value = "0", message = "Количество не может быть отрицательным")
    @NotNull(message = "Количество не может быть пустым")
    private int countInBerlin;

    @DecimalMin(value = "0", message = "Количество не может быть отрицательным")
    @NotNull(message = "Количество не может быть пустым")
    private int countInTokyo;

    @DecimalMin(value = "0", message = "Количество не может быть отрицательным")
    @NotNull(message = "Количество не может быть пустым")
    private int countInStockholm;

    public Product() {

    }

    public Product(String type, String name, String description, int countInMoscow, int countInKazan, int countInBerlin,
                   int countInTokyo, int countInStockholm){
        //Не допускает создание объекта любого неподходящего типа
        //Чтобы в базе данных не было строк, которые не отображаются ни на одной странице сайте
        if (type.equals("videocards")||type.equals("motherboards")||type.equals("cpu")||type.equals("ssd")||
        type.equals("hdd")||type.equals("ram")||type.equals("coolers")||type.equals("cases")||type.equals("fans")||type.equals("power"))
            this.type = type;
        else
            this.type = null;
        this.name = name;
        this.description = description;
        this.countInMoscow=countInMoscow;
        this.countInKazan=countInKazan;
        this.countInBerlin=countInBerlin;
        this.countInTokyo=countInTokyo;
        this.countInStockholm=countInStockholm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        //Примечание то же, что и в конструкторе
        if (type.equals("videocards")||type.equals("motherboards")||type.equals("cpu")||type.equals("ssd")||
                type.equals("hdd")||type.equals("ram")||type.equals("coolers")||type.equals("cases")||type.equals("fans")||type.equals("power"))
            this.type = type;
        else
            this.type = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCountInMoscow() {
        return countInMoscow;
    }

    public void setCountInMoscow(int countInMoscow) {
        this.countInMoscow = countInMoscow;
    }

    public int getCountInKazan() {
        return countInKazan;
    }

    public void setCountInKazan(int countInKazan) {
        this.countInKazan = countInKazan;
    }

    public int getCountInBerlin() {
        return countInBerlin;
    }

    public void setCountInBerlin(int countInBerlin) {
        this.countInBerlin = countInBerlin;
    }

    public int getCountInTokyo() {
        return countInTokyo;
    }

    public void setCountInTokyo(int countInTokyo) {
        this.countInTokyo = countInTokyo;
    }

    public int getCountInStockholm() {
        return countInStockholm;
    }

    public void setCountInStockholm(int countInStockholm) {
        this.countInStockholm = countInStockholm;
    }
}
