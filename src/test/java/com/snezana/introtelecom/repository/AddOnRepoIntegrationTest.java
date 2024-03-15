package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.AddOn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AddOnRepoIntegrationTest {

    @Autowired
    private AddOnRepo addOnRepo;

    @Test
    void findAddOnOpt_byAddonCode_whenIsNotEmpty(){
        String addonCode = "ADDCLS";
        String addonDescription = "calls 100 min";
        Optional<AddOn> found = addOnRepo.findByAddonCodeOpt(addonCode);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(AddOn.class)
                .hasValueSatisfying(addOn-> {
                    assertThat(addOn.getAddonCode()).isEqualTo(addonCode);
                    assertThat(addOn.getAddonDescription()).isEqualTo(addonDescription);
                });
    }

    @Test
    void findAddOnOpt_byAddonCode_whenIsEmpty(){
        String addonCode = "UNKNOWN";
        Optional<AddOn> found = addOnRepo.findByAddonCodeOpt(addonCode);
        assertThat(found).isEmpty();

    }

}
