package com.nutri.rest.repository;

import com.nutri.rest.model.DietitianExtraEducationDetails;
import com.nutri.rest.model.DietitianProfile;
import com.nutri.rest.model.LookupValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietitianExtraEducationDetailsRepository extends JpaRepository<DietitianExtraEducationDetails, Long> {
    List<DietitianExtraEducationDetails> findByDietitianProfileId(DietitianProfile dietitianProfile);
    DietitianExtraEducationDetails findByDietitianProfileIdAndQualifiedDegree(DietitianProfile dietitianProfile, LookupValue qualifiedDegree);
}
