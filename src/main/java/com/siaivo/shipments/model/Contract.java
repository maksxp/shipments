package com.siaivo.shipments.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contract_id")
    private int id;

    @Column(name = "contract_number")
    private int contractNumber;

    @Column(name = "contract_date")
    private Date contractDate;


}
