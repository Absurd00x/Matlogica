import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    private static FirefoxDriver initialize() {
        System.setProperty("webdriver.gecko.driver","./geckodriver");
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("/usr/bin/firefox");
        options.addArguments("--headless");
        return new FirefoxDriver(options);
    }
    private FirefoxDriver driver;

    private JFrame constructWindow(int width, int height, String title) {
        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setTitle(title);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        return window;
    }

    private ArrayList<String> enterParameters(String[] numbers, boolean[] checked) {
        StringBuilder query = new StringBuilder(String.format("https://quto.ru/catalog/search/result/?price_min=%s&price_max=%s&volume_min=%s&volume_max=%s&power_min=%s&power_max=%s",
                numbers[0], numbers[1], numbers[2], numbers[3], numbers[4], numbers[5]));
        if (checked[0]) query.append("&countries=russian");
        if (checked[1]) query.append("&countries=european");
        if (checked[2]) query.append("&countries=us");
        if (checked[3]) query.append("&countries=korean");
        if (checked[4]) query.append("&countries=japanese");
        if (checked[5]) query.append("&countries=chinese");
        if (checked[6]) query.append("&engine_type=benzin");
        if (checked[7]) query.append("&engine_type=disel");
        if (checked[8]) query.append("&engine_type=hybride");
        if (checked[9]) query.append("&engine_type=electric");
        if (checked[10]) query.append("&control=fr");
        if (checked[11]) query.append("&control=f");
        if (checked[12]) query.append("&control=r");
        if (checked[13]) query.append("&transmission=mex");
        if (checked[14]) query.append("&transmission=avt");
        if (checked[15]) query.append("&transmission=robot");
        if (checked[16]) query.append("&transmission=variator");

        driver.get(query.toString());

        List<WebElement> modelNames = driver.findElementsByClassName("search_result_model");
        ArrayList<String> res = new ArrayList<>();
        for(WebElement modelName : modelNames) {
            String[] data = modelName.getText().split("\n");
            res.add(String.format("%s %s", data[0], data[1]));
        }

        return res;
    }

    private void displayInterface() {
        JFrame mainWindow = constructWindow(770, 300, "Подборка автомобиля");
        mainWindow.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                driver.quit();
            }
        });

        JPanel numericPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        // price
        JLabel fromPriceLabel = new JLabel("                                  от");
        JLabel toPriceLabel = new JLabel("                                 до");
        JLabel rublesLabel = new JLabel("рублей");
        SpinnerModel botPriceSpinner = new SpinnerNumberModel(0, 0, 23534000, 1000);
        SpinnerModel topPriceSpinner = new SpinnerNumberModel(4000000, 0, 23534000, 1000);
        JSpinner selectBotPrice = new JSpinner(botPriceSpinner);
        JSpinner selectTopPrice = new JSpinner(topPriceSpinner);



        //0.9-3.5 liters
        // volume
        JLabel fromVolumeLabel = new JLabel("                                  от");
        JLabel toVolumeLabel = new JLabel("                                 до");
        JLabel litersLabel = new JLabel("литров");
        SpinnerModel botVolumeSpinner = new SpinnerNumberModel(0, 0, 3.5, 0.1);
        SpinnerModel topVolumeSpinner = new SpinnerNumberModel(3.5, 0, 3.5, 0.1);
        JSpinner selectBotVolume = new JSpinner(botVolumeSpinner);
        JSpinner selectTopVolume = new JSpinner(topVolumeSpinner);


        // 11-340 hf
        // power
        JLabel fromPowerLabel = new JLabel("                                  от");
        JLabel toPowerLabel = new JLabel("                                 до");
        JLabel hfLabel = new JLabel("лошадиных сил");
        SpinnerModel botPowerSpinner = new SpinnerNumberModel(11, 11, 340, 1);
        SpinnerModel topPowerSpinner = new SpinnerNumberModel(340, 11, 340, 1);
        JSpinner selectBotPower = new JSpinner(botPowerSpinner);
        JSpinner selectTopPower = new JSpinner(topPowerSpinner);


        // dealer
        JPanel dealerPanel = new JPanel();
        dealerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel dealerLabel = new JLabel("Производители");
        JCheckBox[] dealersCheckboxes = new JCheckBox[]{new JCheckBox("Российские"), new JCheckBox("Европейские"),
                new JCheckBox("Американские"), new JCheckBox("Корейские"), new JCheckBox("Японские"),
                new JCheckBox("Китайские")};

        dealerPanel.add(dealerLabel);
        for (JCheckBox checkBox : dealersCheckboxes)
            dealerPanel.add(checkBox);

        // engine type
        JPanel enginePanel = new JPanel();
        enginePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel engineTypeLabel = new JLabel("Тип двигателя");
        JCheckBox[] engineTypesCheckboxes = new JCheckBox[]{new JCheckBox("Бензиновый"),
                new JCheckBox("Дизельный"), new JCheckBox("Гибридный"), new JCheckBox("Электрический")};

        enginePanel.add(engineTypeLabel);
        for(JCheckBox checkBox : engineTypesCheckboxes)
            enginePanel.add(checkBox);

        // drive type
        JPanel drivePanel = new JPanel();
        drivePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel driveTypeLabel = new JLabel("Привод");
        JCheckBox[] driveTypeCheckboxes = new JCheckBox[]{new JCheckBox("Полный"),
                new JCheckBox("Передний"), new JCheckBox("Задний")};

        drivePanel.add(driveTypeLabel);
        for(JCheckBox checkBox : driveTypeCheckboxes)
            drivePanel.add(checkBox);

        // transmission type
        JPanel transmissionPanel = new JPanel();
        transmissionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel transmissionTypeLabel = new JLabel("Коробка передач");
        JCheckBox[] transmissionTypeCheckboxes = new JCheckBox[]{new JCheckBox("Механичкская"),
                new JCheckBox("\"Автомат\""), new JCheckBox("\"Робот\""), new JCheckBox("Вариатор")};

        transmissionPanel.add(transmissionTypeLabel);
        for(JCheckBox checkBox : transmissionTypeCheckboxes)
            transmissionPanel.add(checkBox);

        JCheckBox[][] checkBoxes = new JCheckBox[][]{dealersCheckboxes, engineTypesCheckboxes, driveTypeCheckboxes,
                transmissionTypeCheckboxes};

        // buttons
        JButton goButton = new JButton("Подобрать!");
        goButton.addActionListener(e -> {
            String[] numbers = new String[]{selectBotPrice.getValue().toString(), selectTopPrice.getValue().toString(),
                    selectBotVolume.getValue().toString(), selectTopVolume.getValue().toString(),
                    selectBotPower.getValue().toString(), selectTopPower.getValue().toString()};
            boolean[] checked = new boolean[17];

            // Electric cars cannot be found if volume is specified
            if (numbers[2].equals("0.0")) numbers[2] = "";
            if (numbers[3].equals("3.5")) numbers[3] = "";

            int index = 0;
            for (JCheckBox[] checkBoxArray : checkBoxes)
                for (JCheckBox checkBox : checkBoxArray)
                    checked[index++] = checkBox.isSelected();

            ArrayList<String> matched = enterParameters(numbers, checked);

            if (matched.isEmpty())
                JOptionPane.showMessageDialog(mainWindow,"Не найдены машины по Вашему запросу","Ошибка!",JOptionPane.ERROR_MESSAGE);
            else {
                JDialog popup = new JDialog(mainWindow, "Могу предложить следующие модели", true);
                popup.setSize(350, matched.size() * 17 + 75);
                popup.setLocationRelativeTo(mainWindow);
                popup.setLayout(new BoxLayout(popup.getContentPane(), BoxLayout.PAGE_AXIS));
                String[] matchedArray = new String[matched.size()];
                for (int i = 0; i < matched.size(); ++i)
                    matchedArray[i] = matched.get(i);
                JList<String> carsList = new JList<>(matchedArray);
                popup.add(Box.createVerticalStrut(5));
                popup.add(carsList);
                JButton okButton = new JButton("Ок");
                popup.add(Box.createVerticalStrut(10));
                okButton.addActionListener(ex -> popup.dispose());
                popup.add(okButton);
                popup.add(Box.createVerticalStrut(5));
                popup.setVisible(true);
            }

        });

        JButton exitButton = new JButton("Выход");
        exitButton.addActionListener(e -> mainWindow.dispose());
        JButton dropButton = new JButton("Сброс");
        dropButton.addActionListener(e -> {
            for (JCheckBox[] checkBoxArray : checkBoxes)
                for (JCheckBox checkBox : checkBoxArray)
                    checkBox.setSelected(false);
        });

        buttonPanel.add(goButton);
        buttonPanel.add(dropButton);
        buttonPanel.add(exitButton);

        numericPanel.add(fromPriceLabel);
        numericPanel.add(selectBotPrice);
        numericPanel.add(toPriceLabel);
        numericPanel.add(selectTopPrice);
        numericPanel.add(rublesLabel);

        numericPanel.add(fromVolumeLabel);
        numericPanel.add(selectBotVolume);
        numericPanel.add(toVolumeLabel);
        numericPanel.add(selectTopVolume);
        numericPanel.add(litersLabel);

        numericPanel.add(fromPowerLabel);
        numericPanel.add(selectBotPower);
        numericPanel.add(toPowerLabel);
        numericPanel.add(selectTopPower);
        numericPanel.add(hfLabel);

        numericPanel.setLayout(new GridLayout(3, 5, 0, 10));

        mainWindow.setLayout(new BoxLayout(mainWindow.getContentPane(), BoxLayout.PAGE_AXIS));
        mainWindow.add(numericPanel);
        mainWindow.add(Box.createVerticalStrut(10));
        mainWindow.add(dealerPanel);
        mainWindow.add(enginePanel);
        mainWindow.add(drivePanel);
        mainWindow.add(transmissionPanel);
        mainWindow.add(buttonPanel);


        mainWindow.setVisible(true);
    }

    public static void main(String[] args) {
        Main mc = new Main();
        mc.driver = initialize();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> mc.driver.quit()));
        mc.displayInterface();
    }
}
