package com.dognose.platform.verification;

import java.math.BigDecimal;

public interface NosePrintMatcher {

    NosePrintMatchResult verify(String referenceImageUrl, String probeImageUrl, BigDecimal threshold);
}
