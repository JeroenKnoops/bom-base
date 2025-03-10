/*
 * Copyright (c) 2020-2021, Koninklijke Philips N.V., https://www.philips.com
 * SPDX-License-Identifier: MIT
 */

package com.philips.research.bombase;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bom-base")
public class ConfigProperties {
    private boolean scanLicenses = true;

    public boolean isScanLicenses() {
        return scanLicenses;
    }

    public ConfigProperties setScanLicenses(boolean scanLicenses) {
        this.scanLicenses = scanLicenses;
        return this;
    }
}
