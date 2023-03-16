package dtos;

import loans.LoanPaymentInfo;

public class DTOlender {

    private String lenderName;
    private int LenderFund; //KEREN
    private int LenderInterest; //RIBIT
    private int LenderLoanPayment; //fund + interest (RIBIT + KEREN)
    private int LenderStartYaz;
    private int LenderLastPayment;
    private int restPayToCustomer;
    private String idLoan;
    private String loanStatus;
    private int loanInterest;
    private int loanPayEveryYaz;
    private int fundRest;
    private boolean isForSell;
    private String loanOwner;
    private boolean Check;

    DTOlender(String lenderName, LoanPaymentInfo info, DTOLoan loan){
        this.lenderName = lenderName;
        this.LenderFund = info.getFund();
        this.LenderInterest = info.getInterest();
        this.LenderLoanPayment = info.getLoanPayment();
        this.LenderStartYaz = info.getStartYaz();
        this.LenderLastPayment = info.getLoanPayment();
        this.restPayToCustomer = info.getRestPayToCustomer();
        this.idLoan = loan.getIdLoan();
        this.loanStatus = loan.getLoanStatus().getName();
        this.loanInterest = loan.getInterest();
        this.loanPayEveryYaz = loan.getPaysEveryYaz();
        this.isForSell = info.isIfForSell();
        this.loanOwner = loan.getLoanOwner();
        this.fundRest = info.getFundRest();
        this.Check = false;
    }

    public int getFundRest() {
        return fundRest;
    }

    public String getLoanOwner() {
        return loanOwner;
    }

    public boolean isForSell() {
        return isForSell;
    }

    public int getRestPayToCustomer(){
        return restPayToCustomer;
    }

    public int getLoanInterest() {
        return loanInterest;
    }

    public int getLoanPayEveryYaz() {
        return loanPayEveryYaz;
    }

    public String getIdLoan() {
        return idLoan;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public String getLenderName() {
        return lenderName;
    }

    public int getLenderFund() {
        return LenderFund;
    }

    public int getLenderLastPayment() {
        return LenderLastPayment;
    }

    public int getLenderInterest() {
        return LenderInterest;
    }

    public int getLenderLoanPayment() {
        return LenderLoanPayment;
    }

    public int getLenderStartYaz() {
        return LenderStartYaz;
    }

    public void setSelected(boolean selected) {
        this.Check = selected;
    }

    public boolean getCheckProperty() {
        return Check;
    }
}
