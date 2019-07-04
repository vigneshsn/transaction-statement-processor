package com.rabobank.statementprocessor.models;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@XmlRootElement(name = "record")
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {

    @CsvBindByName(column = "Reference")
    @XmlAttribute(name = "reference")
    @EqualsAndHashCode.Include
    private String id;

    @CsvBindByName(column = "Account Number")
    @XmlElement(name = "accountNumber")
    private String iban;

    @CsvBindByName(column = "Description")
    @XmlElement(name = "description")
    private String description;

    @CsvBindByName(column = "Start Balance")
    @XmlElement(name = "startBalance")
    private BigDecimal startBalance;

    @CsvBindByName(column = "End Balance")
    @XmlElement(name = "endBalance")
    private BigDecimal endBalance;

    @CsvBindByName(column = "Mutation")
    @XmlElement(name = "mutation")
    private BigDecimal mutation;
}
