package com.example.calculation_service.services;

import com.example.calculation_service.dtos.CreditRequest;
import com.example.calculation_service.dtos.CreditRestrictionsDTO;
import com.example.calculation_service.entities.TotalCosts;
import com.example.calculation_service.repositories.TotalCostRepository;
import com.example.common_utils.enums.CreditType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CalculateService {

    @Autowired
    TotalCostRepository totalCostRepository;

    public float getMinAnnualRate(CreditType type) {
        return switch (type) {
            case FIRSTHOME -> 3.5F;
            case SECONDHOME -> 4;
            case COMERCIAL -> 5;
            case REMODELING -> 4.5F;
        };
    }

    public float getMaxAnnualRate(CreditType type) {
        return switch (type) {
            case FIRSTHOME -> 5;
            case SECONDHOME -> 6;
            case COMERCIAL -> 7;
            case REMODELING -> 6;
        };
    }

    public int getMaxFinancingMount(CreditRequest request) {
        CreditType type = CreditType.valueOf(request.getCreditType());
        double percentage = switch (type) {
            case FIRSTHOME -> 0.8;
            case SECONDHOME -> 0.7;
            case COMERCIAL -> 0.6;
            case REMODELING -> 0.5;
        };
        return (int) (request.getPropertyValue() * percentage);
    }

    public int getMaxLoanPeriod(CreditType type) {
        return switch (type) {
            case FIRSTHOME -> 30;
            case SECONDHOME -> 20;
            case COMERCIAL -> 25;
            case REMODELING -> 15;
        };
    }

    public CreditRestrictionsDTO calculateRestrictions(CreditRequest request) {
        CreditRestrictionsDTO restrictions = new CreditRestrictionsDTO();
        CreditType type = CreditType.valueOf(request.getCreditType());

        restrictions.setMaxLoanPeriod((float) getMaxLoanPeriod(type));
        restrictions.setMaxFinancingMount((float) getMaxFinancingMount(request));
        restrictions.setMinAnnualRate(getMinAnnualRate(type));
        restrictions.setMaxAnnualRate(getMaxAnnualRate(type));
        return restrictions;
    }


    //Método para obtener la cuota mensual de un crédito dado sus parámetros
    public int getCreditInstallment(CreditRequest credit){
        int n = credit.getLoanPeriod()*12;
        double annualRate = credit.getAnnualRate();
        double rate = annualRate / 12 / 100;
        double compoundInterest = Math.pow(1 + rate, n);
        int capital = credit.getCreditMount();
        return (int) (capital*(rate*compoundInterest)/(compoundInterest - 1));
    }

    public List<Integer> simulateCreditInstallments(CreditRequest credit) {
        CreditType type = CreditType.valueOf(credit.getCreditType());
        credit.setAnnualRate(getMinAnnualRate(type));
        int minInstallment = getCreditInstallment(credit);

        credit.setAnnualRate(credit.getAnnualRate());
        int requestedInstallment = getCreditInstallment(credit);

        credit.setAnnualRate(getMaxAnnualRate(type));
        int maxInstallment = getCreditInstallment(credit);

        return Arrays.asList(minInstallment, requestedInstallment , maxInstallment);
    }

    public TotalCosts calculate(CreditRequest credit) {
        TotalCosts costs = new TotalCosts();

        costs.setInstallment(getCreditInstallment(credit));
        costs.setCreditInsurance((int) ( costs.getInstallment() * 0.003 ));
        costs.setFireInsurance(20000);
        costs.setAdministrativeFee((int) ( credit.getCreditMount() * 0.01) );
        costs.setMonthlyTotal( costs.getInstallment() + costs.getCreditInsurance() + costs.getFireInsurance() );
        costs.setFinalTotal( ( costs.getMonthlyTotal() * credit.getLoanPeriod() * 12 ) + costs.getAdministrativeFee() );

        return costs;
    }

    public void save(TotalCosts totalCosts) {
        totalCostRepository.save(totalCosts);
    }

    public TotalCosts calculateAndSaveTotalCost(CreditRequest request) {
        TotalCosts totalCosts = calculate(request);
        save(totalCosts);
        return totalCosts;
    }
}
