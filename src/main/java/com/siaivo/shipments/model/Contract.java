package com.siaivo.shipments.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contract_id")
    private int id;

    @Column(name = "contract_number")
    @NotEmpty(message = "*Please provide user name")
    private int contractNumber;

    @Column(name = "contract_date")
    @NotEmpty(message = "*Please provide contract date")
    private Date contractDate;

    @Column(name = "comment")
    private String comment;

}
