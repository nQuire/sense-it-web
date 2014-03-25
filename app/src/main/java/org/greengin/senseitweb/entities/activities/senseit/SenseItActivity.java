package org.greengin.senseitweb.entities.activities.senseit;



import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.greengin.senseitweb.entities.data.DataCollectionActivity;

@Entity
public class SenseItActivity extends DataCollectionActivity<SenseItSeries, SenseItAnalysis> {

	

	@OneToOne(orphanRemoval = true, cascade=CascadeType.ALL)
    @Getter
    @Setter
    SenseItProfile profile = new SenseItProfile();

}
