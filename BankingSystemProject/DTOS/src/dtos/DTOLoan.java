package dtos;

import customers.Customer;
import loans.Loan;
import loans.LoanPaymentInfo;
import loans.status.StatusENUM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DTOLoan implements DTO {

    private String IdLoan;
    private String loanOwner;
    private String category;
    private int loanAmount;
    private int totalYazTime;
    private int paysEveryYaz;
    private int startActiveYaz;
    private int lastPayYaz;
    private int endingYaz;
    private int notPayedYazs;
    private int interest;
    private StatusENUM loanStatus;
    private List<DTOlender> lenders;
    private int totalFund = 0; //all the money we achieve for this loan
    private int totalInterest = 0; //total RIBIT
    private int totalAmountReturn = 0; //total KEREN + RIBIT that we need to return
    private int amountEveryPulse;
    private int fundLeft =0;
    private boolean Check;
    private int debt;


    public DTOLoan(Loan loan)
    {
        this.IdLoan = loan.getID();
        this.loanOwner = loan.getLoanOwner();
        this.category = loan.getLoanCategory();
        this.loanAmount = loan.getLoanAmount();
        this.interest = loan.getLoanInterest();
        this.loanStatus = loan.getLoanStatus();
        this.startActiveYaz = loan.getLoanYaz().getStartActiveYaz();
        this.endingYaz = loan.getLoanYaz().getEndingYaz();
        this.totalYazTime = loan.getLoanYaz().getTotalYazTime();
        this.paysEveryYaz = loan.getLoanYaz().getPaysEveryYaz();
        this.lastPayYaz = loan.getLoanYaz().getLastPayYaz();
        buildLendersList(loan.getLenders());
        this.amountEveryPulse = loan.getTotalAmountToPayOnce();
        this.totalInterest = loan.getTotalInterest();
        this.totalFund = loan.getTotalFund();
        this.totalAmountReturn = totalFund+totalInterest;
        this.notPayedYazs = loan.getLoanYaz().getNotPayPulseYaz();
        this.fundLeft = this.loanAmount - this.totalFund;
        this.debt = loan.getDebt();
        this.Check = false;
    }

    public void buildLendersList(Map<Customer, LoanPaymentInfo> tempLenders){
        lenders = new ArrayList<>();
        for(Map.Entry<Customer, LoanPaymentInfo> lenderz: tempLenders.entrySet()) {
            DTOlender loanInfo = new DTOlender(lenderz.getKey().getName(), lenderz.getValue(), this);
            lenders.add(loanInfo);
        }
    }

    public String getIdLoan(){
        return IdLoan;
    }

    public String getLoanOwner(){
        return loanOwner;
    }

    public String getCategory() {
        return category;
    }

    public int getInterest() {
        return interest;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public int getEndingYaz() {
        return endingYaz;
    }

    public int getStartActiveYaz() {
        return startActiveYaz;
    }

    public int timeToPayNextPayment(){
        return (lastPayYaz + paysEveryYaz);
    }

    public int getFundLeft() { return fundLeft;}

    public StatusENUM getLoanStatus() {
        return loanStatus;
    }

    public String getLoanStatusString() {
        return loanStatus.getName();}

    public List<DTOlender> getLenders() {
        return lenders;
    }

    public int getTotalInterest() {
        return totalInterest;
    }

    public int getTotalFund() {
        return totalFund;
    }

    public int getTotalAmountReturn() {
        return totalAmountReturn;
    }

    public int getLastPayYaz() {
        return lastPayYaz;
    }

    public int getPaysEveryYaz() {
        return paysEveryYaz;
    }

    public int getTotalYazTime() {
        return totalYazTime;
    }

    public int getNotPayedYazs() {
        return notPayedYazs;
    }

    public int getAmountEveryPulse() {
        return amountEveryPulse;
    }

    public boolean getCheckProperty() {
        return Check;
    }

    public void setSelected(boolean selected) {
        this.Check = selected;
    }

    public boolean getSelected(){
        return this.Check;
    }

    public int getDebt() {
        return debt;
    }
}
