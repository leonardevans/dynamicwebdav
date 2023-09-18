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
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Simulation of a simple data access layer.
 *
 * @author  lemutugi.com
 * @version 1.0.0 20210629
 */
@Service
class DynamicService {

    private DynamicData dynamicData;

    @PostConstruct
    private void initDynamicData() throws IOException {
        this.dynamicData = new DynamicData();
        String directoryPath = "/templates";

        // Get a list of file names in the directory
        List<String> fileNames = listFilesInDirectory(directoryPath);

        // Create a list of byte arrays to store the directory content
        List<byte[]> directoryContents = new ArrayList<>();
        List<String> filepaths = new ArrayList<>();

        for (String fileName : fileNames) {
            try (InputStream inputStream = DynamicService.class.getResourceAsStream(directoryPath + "/" + fileName)) {
                byte[] fileContent = inputStream.readAllBytes();
                directoryContents.add(fileContent);
                filepaths.add( directoryPath+ "/" + fileName);
            }
        }

        this.dynamicData.setData(directoryContents);
        this.dynamicData.setFilepaths(filepaths);
        this.dynamicData.setLastModified(new Date());
    }

    DynamicData readDynamicData() {
        return this.dynamicData;
    }

    void saveDynamicData(DynamicData dynamicData) {
        this.dynamicData.setData(dynamicData.getData());
        this.dynamicData.setLastModified(new Date());
    }

    private List<String> listFilesInDirectory(String directoryPath) {
        List<String> fileNames = new ArrayList<>();
        InputStream resourceAsStream = DynamicService.class.getResourceAsStream(directoryPath);

        if (resourceAsStream != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
                String file;
                while ((file = br.readLine()) != null) {
                    fileNames.add(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileNames;
    }
}