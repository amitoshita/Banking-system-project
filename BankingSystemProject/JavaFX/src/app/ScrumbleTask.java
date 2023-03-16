package app;
import dtos.DTO;
import dtos.DTOLoan;
import javafx.concurrent.Task;

import java.util.List;

public class ScrumbleTask extends Task<Boolean> {

    private AppController controller;
    private int customerAmount;
    private String customerName;
    private List<DTOLoan> list;
    private int precent;
    public ScrumbleTask(List<DTOLoan> list, String name, int amount, AppController appController, int precent){
        this.controller = appController;
        this.customerAmount = amount;
        this.customerName = name;
        this.list = list;
        this.precent = precent;
    }

    @Override
    protected Boolean call() throws Exception {
        controller.getTransport().divideTheLoansMoney(list, customerAmount, customerName, precent);
        for(int i = 0; i < 5; i++){
            Thread.sleep(500);
            updateProgress(i + 1,5);
            updateMessage("Loading scramble action...");
        }

        return true;
    }
}
