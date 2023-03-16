package loans;

import customers.Customer;

import java.lang.management.BufferPoolMXBean;
import java.util.HashMap;
import java.util.Map;

public class LoanYaz{

    private int totalYazTime;
    private int paysEveryYaz;
    private int startActiveYaz;
    private int NextPayment = 0;
    private int lastPayYaz;
    private int endingYaz;
    private int paymentPulseYaz;
    private int notPayPulseYaz = 0;
    private Map<Integer, Boolean> PayedOrNot = new HashMap<>();

    public LoanYaz(int totalYazTime, int paysEveryYaz){
        this.totalYazTime = totalYazTime;
        this.paysEveryYaz = paysEveryYaz;
        startActiveYaz = 0;
        lastPayYaz = 1;
        endingYaz=0;
        paymentPulseYaz = this.totalYazTime/this.paysEveryYaz;
        int count = 0;
        for(int i = 0; i < paymentPulseYaz; i++){
            PayedOrNot.put(startActiveYaz + count, false);
            count+=paysEveryYaz;
        }
    }

    public void setBooleanPaymentToTrue(){
        for(Map.Entry<Integer, Boolean> k : PayedOrNot.entrySet()){
            if (k.getValue() == false)
            {
                k.setValue(true);
                return;
            }
        }
    }

    public Map<Integer, Boolean> getPayedOrNot() {
        return PayedOrNot;
    }

    public int getEndingYaz(){
        return endingYaz;
    }

    public int getTotalYazTime(){
        return totalYazTime;
    }

    public int getPaysEveryYaz(){
        return paysEveryYaz;
    }

    public int getStartActiveYaz(){
        return startActiveYaz;
    }

    public int getLastPayYaz(){
        return lastPayYaz;
    }

    public void setEndingYaz(int endingYaz) {
        this.endingYaz = endingYaz;
    }

    public void setStartActiveYaz(int start){
        this.startActiveYaz = start;
        this.lastPayYaz= start;
    }


    public void setLastPayYaz(int lastPayYaz){
        this.lastPayYaz = lastPayYaz;
    }

    public void setNextPayment(int nextPayment){
        if(nextPayment==0)
        {
            nextPayment=startActiveYaz;
        }
        this.NextPayment += nextPayment;
    }

    public int getNextPayment() {
        return NextPayment;
    }

    public int getPaymentPulseYaz() {
        return paymentPulseYaz;
    }

    public void setNotPayPulseYaz(int notPayPulseYaz) {
        this.notPayPulseYaz = notPayPulseYaz;
    }

    public int getNotPayPulseYaz() {
        return notPayPulseYaz;
    }
}
