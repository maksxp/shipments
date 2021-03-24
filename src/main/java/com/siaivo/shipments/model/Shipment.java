package com.siaivo.shipments.model;

import com.siaivo.shipments.repository.ShipmentRepository;
import com.siaivo.shipments.service.ProductForShipmentService;
import com.siaivo.shipments.service.ShipmentService;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name="contract_id")
    private Contract contract;

    @Column(name = "planned_loading_date")
    private String plannedLoadingDate;

    @Column(name = "actual_loading_date")
    private String actualLoadingDate;

    @Column(name = "planned_unloading_date")
    private String plannedUnloadingDate;

    @Column(name = "actual_unloading_date")
    private String actualUnloadingDate;

    @Column(name = "logistic_instruction_status")
    private String logisticInstructionStatus;

    @Column(name = "labels_status")
    private String labelsStatus;

    @Column(name = "shipment_comment")
    private String shipmentComment;

    @Column(name = "invoice_comment")
    private String invoiceComment;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "truck_number")
    private int truckNumber;

    @Column(name = "invoice_first_part_sum")
    private BigDecimal invoiceFirstPartSum;

    @Column(name = "invoice_second_part_sum")
    private BigDecimal invoiceSecondPartSum;

    @Column(name = "planned_payment_date_of_first_part_sum")
    private String plannedPaymentDateOfFirstPartSum;

    @Column(name = "actual_payment_date_of_first_part_sum")
    private String actualPaymentDateOfFirstPartSum;

    @Column(name = "planned_payment_date_of_second_part_sum")
    private String plannedPaymentDateOfSecondPartSum;

    @Column(name = "actual_payment_date_of_second_part_sum")
    private String actualPaymentDateOfSecondPartSum;

    @Column(name = "planned_payment_date_of_whole_sum")
    private String plannedPaymentDateOfWholeSum;

    @Column(name = "actual_payment_date_of_whole_sum")
    private String actualPaymentDateOfWholeSum;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shipment")
    private List<ProductForShipment> productsForShipment;

    @Column(name = "is_fulfilled")
    private Boolean isFulfilled;

    @Column(name = "destination_country")
    private String destinationCountry;

    @Column(name = "destination_place")
    private String destinationPlace;

    private String sumForPayIdentity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getPlannedLoadingDate() {
        return plannedLoadingDate;
    }

    public void setPlannedLoadingDate(String plannedLoadingDate) {
        this.plannedLoadingDate = plannedLoadingDate;
    }

    public String getActualLoadingDate() {
        return actualLoadingDate;
    }

    public void setActualLoadingDate(String actualLoadingDate) {
        this.actualLoadingDate = actualLoadingDate;
    }

    public String getPlannedUnloadingDate() {
        return plannedUnloadingDate;
    }

    public void setPlannedUnloadingDate(String plannedUnloadingDate) { this.plannedUnloadingDate = plannedUnloadingDate;
    }

    public String getActualUnloadingDate() {
        return actualUnloadingDate;
    }

    public void setActualUnloadingDate(String actualUnloadingDate) {
        this.actualUnloadingDate = actualUnloadingDate;
    }

    public String getLogisticInstructionStatus() {
        return logisticInstructionStatus;
    }

    public void setLogisticInstructionStatus(String logisticInstructionStatus) {
        this.logisticInstructionStatus = logisticInstructionStatus;
    }

    public String getLabelsStatus() {
        return labelsStatus;
    }

    public void setLabelsStatus(String labelsStatus) {
        this.labelsStatus = labelsStatus;
    }

    public String getShipmentComment() {
        return shipmentComment;
    }

    public void setShipmentComment(String shipmentComment) {
        this.shipmentComment = shipmentComment;
    }

    public String getInvoiceComment() {
        return invoiceComment;
    }

    public void setInvoiceComment(String invoiceComment) {
        this.invoiceComment = invoiceComment;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber;  }

    public BigDecimal getInvoiceFirstPartSum() {
        return invoiceFirstPartSum;
    }

    public void setInvoiceFirstPartSum(BigDecimal invoiceFirstPartSum) {
        this.invoiceFirstPartSum = invoiceFirstPartSum;
    }

    public BigDecimal getInvoiceSecondPartSum() {
        return invoiceSecondPartSum;
    }

    public void setInvoiceSecondPartSum(BigDecimal invoiceSecondPartSum) {
        this.invoiceSecondPartSum = invoiceSecondPartSum;
    }

    public String getPlannedPaymentDateOfFirstPartSum() {
        return plannedPaymentDateOfFirstPartSum;
    }

    public void setPlannedPaymentDateOfFirstPartSum(String plannedPaymentDateOfFirstPartSum) {
        this.plannedPaymentDateOfFirstPartSum = plannedPaymentDateOfFirstPartSum;
    }

    public String getActualPaymentDateOfFirstPartSum() {
        return actualPaymentDateOfFirstPartSum;
    }

    public void setActualPaymentDateOfFirstPartSum(String actualPaymentDateOfFirstPartSum) {
        this.actualPaymentDateOfFirstPartSum = actualPaymentDateOfFirstPartSum;
    }

    public String getPlannedPaymentDateOfSecondPartSum() {
        return plannedPaymentDateOfSecondPartSum;
    }

    public void setPlannedPaymentDateOfSecondPartSum(String plannedPaymentDateOfSecondPartSum) {
        this.plannedPaymentDateOfSecondPartSum = plannedPaymentDateOfSecondPartSum;
    }

    public String getActualPaymentDateOfSecondPartSum() {
        return actualPaymentDateOfSecondPartSum;
    }

    public void setActualPaymentDateOfSecondPartSum(String actualPaymentDateOfSecondPartSum) {
        this.actualPaymentDateOfSecondPartSum = actualPaymentDateOfSecondPartSum;
        if (getUnpaidSumOfFirstPartSum()!=null&&getUnpaidSumOfFirstPartSum().equals(BigDecimal.ZERO))
        actualPaymentDateOfWholeSum = actualPaymentDateOfSecondPartSum;
    }

    public String getPlannedPaymentDateOfWholeSum() {
        return plannedPaymentDateOfWholeSum;
    }

    public void setPlannedPaymentDateOfWholeSum(String plannedPaymentDateOfWholeSum) {
        this.plannedPaymentDateOfWholeSum = plannedPaymentDateOfWholeSum;
    }

    public String getActualPaymentDateOfWholeSum() {
        return actualPaymentDateOfWholeSum;
    }

    public void setActualPaymentDateOfWholeSum(String actualPaymentDateOfWholeSum) {
        this.actualPaymentDateOfWholeSum = actualPaymentDateOfWholeSum;
    }

    public Boolean getIsFulfilled() {
        return isFulfilled;
    }

    public void setIsFulfilled (Boolean isFulfilled) {
        this.isFulfilled = isFulfilled;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationPlace() {
        return destinationPlace;
    }

    public void setDestinationPlace(String destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public String getSumForPayIdentity() {
        return sumForPayIdentity;
    }

    public void deleteProductForShipment (Product product) {
        List <ProductForShipment> productForShipmentList;
        productForShipmentList = getProductsForShipment().stream().filter(productForShipment ->
                productForShipment.getProduct().equals(product))
                .collect(Collectors.toList());
        setProductsForShipment(productForShipmentList);
    }

    public void setSumForPayIdentity(String sumForPayIdentity) {
        if (null == this.sumForPayIdentity){
            this.sumForPayIdentity = sumForPayIdentity;
        } else if ("first".equals(this.sumForPayIdentity) && "second".equals(sumForPayIdentity)) {
            this.sumForPayIdentity = "firstAndSecond";
        } else if ("second".equals(this.sumForPayIdentity) && "first".equals(sumForPayIdentity)) {
            this.sumForPayIdentity = "firstAndSecond";
        } else {
            this.sumForPayIdentity = sumForPayIdentity;
        }
    }

    public String getActualFullSettlementDate(){
        String paymentTerms = this.contract.getPaymentTerms();
        if (paymentTerms.equals("оплата частинами")&&(actualPaymentDateOfFirstPartSum!=null&&!actualPaymentDateOfFirstPartSum.equals(""))){
            return this.actualPaymentDateOfSecondPartSum;
        } else {
            return this.actualPaymentDateOfWholeSum;
        }
    }

    public String getPlannedFullSettlementDate(){
        String paymentTerms = this.contract.getPaymentTerms();
        if (paymentTerms.equals("оплата частинами")){
            return this.plannedPaymentDateOfSecondPartSum;
        } else {
            return this.plannedPaymentDateOfWholeSum;
        }
    }

    public int getWeekOfPlannedLoadingDate() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        if (getPlannedLoadingDate()!=null&&!getPlannedLoadingDate().equals("")) {
            Date date = df.parse(getPlannedLoadingDate());
            Calendar cal = Calendar.getInstance(Locale.FRANCE);
            cal.setTime(date);
            return cal.get(Calendar.WEEK_OF_YEAR);
        } else {
            return 0;
        }
    }

    public int getMonthOfPlannedLoadingDate() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        if (getPlannedLoadingDate()!=null&&!getPlannedLoadingDate().equals("")) {
            Date date = df.parse(getPlannedLoadingDate());
            Calendar cal = Calendar.getInstance(Locale.FRANCE);
            cal.setTime(date);
            return cal.get(Calendar.MONTH);
        } else {
            return 0;
        }
    }

    public int getWeekOfPlannedUnloadingDate() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        if (getPlannedUnloadingDate()!=null&&!getPlannedUnloadingDate().equals("")) {
            Date date = df.parse(getPlannedUnloadingDate());
            Calendar cal = Calendar.getInstance(Locale.FRANCE);
            cal.setTime(date);
            return cal.get(Calendar.WEEK_OF_YEAR);
        } else {
            return 0;
        }
    }

    public int getYearOfPlannedLoadingDate() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        if (getPlannedLoadingDate()!=null&&!getPlannedLoadingDate().equals("")) {
            Date date = df.parse(getPlannedLoadingDate());
            Calendar cal = Calendar.getInstance(Locale.FRANCE);
            cal.setTime(date);
            return cal.get(Calendar.YEAR);
        } else {
            return 0;
        }
    }

    public int getYearOfPlannedUnloadingDate() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        if (getPlannedUnloadingDate()!=null&&!getPlannedUnloadingDate().equals("")) {
            Date date = df.parse(getPlannedUnloadingDate());
            Calendar cal = Calendar.getInstance(Locale.FRANCE);
            cal.setTime(date);
            return cal.get(Calendar.YEAR);
        } else {
            return 0;
        }
    }

    public List<ProductForShipment> getProductsForShipment() {
        return productsForShipment.stream().filter(productForShipment -> productForShipment.getQuantity().compareTo(BigDecimal.ZERO)!=0).collect(Collectors.toList());
    }

    public List<ProductForShipment> getAllProductsForShipment() {
        return productsForShipment;
    }

    public void setProductsForShipment(List<ProductForShipment> productsForShipment) {
        this.productsForShipment = productsForShipment;
    }

    public int getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(int truckNumber) {
        this.truckNumber = truckNumber;
    }

    public BigDecimal getInvoiceWholeSum(Shipment shipment) {
        List <BigDecimal> costOfEachProduct = new ArrayList<>();
        shipment.getProductsForShipment().stream().forEach(productForShipment -> costOfEachProduct.add(productForShipment.getQuantity().multiply(productForShipment.getProduct().getPrice())));
        return costOfEachProduct.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getInvoiceWholeSum() {
        List <BigDecimal> costOfEachProduct = new ArrayList<>();
        getProductsForShipment().stream().forEach(productForShipment -> costOfEachProduct.add(productForShipment.getQuantity().multiply(productForShipment.getProduct().getPrice())));
        return costOfEachProduct.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<String> getListOfGoodsPerShipment (Shipment shipment) {
        List <String> listOfGoodsPerShipment = new ArrayList<>();
        shipment.getProductsForShipment().stream().forEach(productForShipment -> listOfGoodsPerShipment.add(productForShipment.getProduct().getCommodity().getCommodityName()+" ( "+productForShipment.getQuantity()+" тонн"+" ) "+productForShipment.getProduct().getPackaging()));

        return listOfGoodsPerShipment;
    }

    public BigDecimal getArrears () throws ParseException {
        List <BigDecimal> arrearsSums = new ArrayList<>();
        if (getContract().getPaymentTerms().equals("оплата частинами")){
            arrearsSums.add(getFirstSumArrears());
            arrearsSums.add(getSecondSumArrears());
        } else {
            arrearsSums.add(getWholeSumArrears());
        }
        return arrearsSums.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getUnpaidSum () throws ParseException{
        List <BigDecimal> unpaidSums  = new ArrayList<>();
        if (getContract().getPaymentTerms().equals("оплата частинами")){
            unpaidSums.add(getUnpaidSumOfFirstPartSum());
            unpaidSums.add(getUnpaidSumOfSecondPartSum());
        } else {
            unpaidSums.add(getUnpaidSumOfWholeSum());
        }
        return unpaidSums.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getFirstSumArrears () throws ParseException {
        BigDecimal firstSumArrears = BigDecimal.ZERO;
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        if (plannedPaymentDateOfFirstPartSum==null||plannedPaymentDateOfFirstPartSum.equals("")) {
        } else if (df.parse(df.format(calendar.getTime())).compareTo(df.parse(this.plannedPaymentDateOfFirstPartSum))>0&&(actualPaymentDateOfFirstPartSum==null||actualPaymentDateOfFirstPartSum.equals(""))){
            firstSumArrears = this.invoiceFirstPartSum;
        }
        return firstSumArrears;
    }

    public BigDecimal getUnpaidSumOfFirstPartSum () {
        BigDecimal unpaidSumOfFirstPartSum = BigDecimal.ZERO;
//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        if (actualPaymentDateOfFirstPartSum==null||actualPaymentDateOfFirstPartSum.equals("")){
            unpaidSumOfFirstPartSum = this.invoiceFirstPartSum;
        }
        return unpaidSumOfFirstPartSum;
    }

    public BigDecimal getSecondSumArrears () throws ParseException {
        BigDecimal secondSumArrears = BigDecimal.ZERO;
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        if (plannedPaymentDateOfSecondPartSum==null||plannedPaymentDateOfSecondPartSum.equals("")) {
        } else if (df.parse(df.format(calendar.getTime())).compareTo(df.parse(this.plannedPaymentDateOfSecondPartSum))>0&&(actualPaymentDateOfSecondPartSum==null||actualPaymentDateOfSecondPartSum.equals(""))){
            secondSumArrears = this.invoiceSecondPartSum;
        }
        return secondSumArrears;
    }

    public BigDecimal getUnpaidSumOfSecondPartSum () {
        BigDecimal unpaidSumOfSecondPartSum = BigDecimal.ZERO;
        if (actualPaymentDateOfSecondPartSum==null||actualPaymentDateOfSecondPartSum.equals("")){
            unpaidSumOfSecondPartSum = this.invoiceSecondPartSum;
        }
        return unpaidSumOfSecondPartSum;
    }

    public BigDecimal getWholeSumArrears () throws ParseException {
        BigDecimal wholeSumArrears = BigDecimal.ZERO;
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        if (plannedPaymentDateOfWholeSum==null||plannedPaymentDateOfWholeSum.equals("")) {
        } else if (df.parse(df.format(calendar.getTime())).compareTo(df.parse(this.plannedPaymentDateOfWholeSum))>0&&(actualPaymentDateOfWholeSum==null||actualPaymentDateOfWholeSum.equals(""))){
            wholeSumArrears = getInvoiceWholeSum();
        }
        return wholeSumArrears;
    }

    public BigDecimal getUnpaidSumOfWholeSum () {
        BigDecimal unpaidSumOfWholeSum = BigDecimal.ZERO;
        if (actualPaymentDateOfWholeSum==null||actualPaymentDateOfWholeSum.equals("")){
            unpaidSumOfWholeSum = getInvoiceWholeSum();
        }
        return unpaidSumOfWholeSum;
    }

    public String getCurrency (){
        if (getContract().getProducts().size()>0) {
            return getContract().getProducts().get(0).getCurrency();
        } else {
            return "";
        }
    }

//    public boolean isLoadedOrAnyPaymentMade () {
//        boolean isLoadedOrAnyPaymentMade = false;
//        if (isLoaded()) {
//            isLoadedOrAnyPaymentMade = true;
//        }
//        if (isFirstPartSumPaid()){
//            isLoadedOrAnyPaymentMade = true;
//        }
//        if (isSecondPartSumPaid()) {
//            isLoadedOrAnyPaymentMade = true;
//        }
//        if (isWholeSumPaid()){
//            isLoadedOrAnyPaymentMade = true;
//        }
//        return isLoadedOrAnyPaymentMade;
//    }

    public boolean isLoadedOrAnyPaymentMade () {
        boolean isLoadedOrAnyPaymentMade = false;
        if (isLoaded()||isAnySumPaid()){
            isLoadedOrAnyPaymentMade = true;
        }
        return isLoadedOrAnyPaymentMade;
    }

    public boolean isLoaded () {
        boolean isLoaded = true;
        if (actualLoadingDate==null || actualLoadingDate.equals("")){
            isLoaded = false;
        }
        return isLoaded;
    }

    public boolean isFirstPartSumPaid () {
        boolean isFirstPartSumPaid = true;
        if (actualPaymentDateOfFirstPartSum==null || actualPaymentDateOfFirstPartSum.equals("")){
            isFirstPartSumPaid = false;
        }
        return isFirstPartSumPaid;
    }

    public boolean isSecondPartSumPaid () {
        boolean isSecondPartSumPaid = true;
        if (actualPaymentDateOfSecondPartSum==null || actualPaymentDateOfSecondPartSum.equals("")){
            isSecondPartSumPaid = false;
        }
        return isSecondPartSumPaid;
    }

    public boolean isWholeSumPaid () {
        boolean isWholeSumPaid = true;
        if (actualPaymentDateOfWholeSum==null || actualPaymentDateOfWholeSum.equals("")){
            isWholeSumPaid = false;
        }
        return isWholeSumPaid;
    }

    public boolean isAnySumPaid () {
        boolean isAnySumPaid = false;
        if (isFirstPartSumPaid()||isSecondPartSumPaid()||isWholeSumPaid()){
            isAnySumPaid =true;
        }
        return isAnySumPaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipment shipment = (Shipment) o;
        return id == shipment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
