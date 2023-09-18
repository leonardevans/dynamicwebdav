/**
 * LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 * im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 * Diese Software unterliegt der Version 2 der Apache License.
 *
 * WebDAV mapping for Spring Boot
 * Copyright (C) 2021 Seanox Software Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.dynamicwebdav.dynamicwebdav.api.dynamic;

import com.dynamicwebdav.dynamicwebdav.api.dynamic.data.DynamicData;
import com.dynamicwebdav.dynamicwebdav.api.financial.data.FinancialCosts;
import com.dynamicwebdav.dynamicwebdav.api.financial.data.FinancialReportSales;
import com.dynamicwebdav.dynamicwebdav.api.financial.data.FinancialReportStatistic;
import com.seanox.webdav.WebDavAttributeMapping;
import com.seanox.webdav.WebDavInputMapping;
import com.seanox.webdav.WebDavMapping;
import com.seanox.webdav.WebDavMappingAttribute;
import com.seanox.webdav.MetaInputStream;
import com.seanox.webdav.MetaOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

/**
 * Example for the integration of webDAV into a RestController.<br>
 * <br>
 * In general, a managed bean is required.<br>
 * There are various annotations for this:<br>
 *     e.g. @Component, @Service, @RestController, ...<br>
 * The methods and annotations for webDAV combine well with @RestController.
 *
 * @author  lemutugi.com
 * @version 1.1.0 20210810
 */
@Profile({"test", "demo"})
@RequiredArgsConstructor
@RestController
class DynamicController {

    private final DynamicService dynamicService;

    private static final String DYNAMIC_DATA ="/dynamic/";
    @WebDavMapping(path=DYNAMIC_DATA, readOnly=false)
    void dynamicDataGet(final MetaOutputStream output) throws IOException {
        final DynamicData dynamicData = this.dynamicService.readDynamicData();

        for (byte[] data : dynamicData.getData()) {
            output.write(data);
        }
    }
    @WebDavInputMapping(path=DYNAMIC_DATA)
    void dynamicDataPut(final MetaInputStream input) throws IOException {
        final DynamicData dynamicData = this.dynamicService.readDynamicData();
        dynamicData.getData().add(input.readAllBytes());
        this.dynamicService.saveDynamicData(dynamicData);
    }
    @WebDavAttributeMapping(path=DYNAMIC_DATA, attribute=WebDavMappingAttribute.ContentLength)
    Integer dynamicDataContentLength() {
        final DynamicData dynamicData = this.dynamicService.readDynamicData();
        int length = 0;
        for (byte[] data : dynamicData.getData()) {
            length += data.length;
        }

        return length;
    }
    @WebDavAttributeMapping(path=DYNAMIC_DATA, attribute=WebDavMappingAttribute.LastModified)
    Date dynamicDataLastModified() {
        return this.dynamicService.readDynamicData().getLastModified();
    }
}