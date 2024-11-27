package com.example.creditcalculate_service.services;

import com.example.creditcalculate_service.dtos.CreditRequest;
import com.example.common_utils.enums.CreditType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CalculateService {

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

    public int getMaxFinancingMount(CreditType type, CreditRequest credit) {
        double percentage = switch (type) {
            case FIRSTHOME -> 0.8;
            case SECONDHOME -> 0.7;
            case COMERCIAL -> 0.6;
            case REMODELING -> 0.5;
        };
        return (int) (credit.getPropertyValue() * percentage);
    }

    public int getMaxLoanPeriod(CreditType type) {
        return switch (type) {
            case FIRSTHOME -> 30;
            case SECONDHOME -> 20;
            case COMERCIAL -> 25;
            case REMODELING -> 15;
        };
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
        credit.setAnnualRate(getMinAnnualRate(credit.getCreditType()));
        int minInstallment = getCreditInstallment(credit);

        credit.setAnnualRate(credit.getAnnualRate());
        int requestedInstallment = getCreditInstallment(credit);

        credit.setAnnualRate(getMaxAnnualRate(credit.getCreditType()));
        int maxInstallment = getCreditInstallment(credit);

        return Arrays.asList(minInstallment, requestedInstallment , maxInstallment);
    }

}
