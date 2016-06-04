/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wfs;

import org.geoserver.config.ServiceFactoryExtension;

public class WFSFactoryExtension extends ServiceFactoryExtension<WFSInfo> {

    protected WFSFactoryExtension() {
        super(WFSInfo.class);
    }

    public <T> T create(Class<T> clazz) {
        return (T) new WFSInfoImpl();
    }

}
