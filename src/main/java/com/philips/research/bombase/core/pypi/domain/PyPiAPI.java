/*
 * Copyright (c) 2020-2021, Koninklijke Philips N.V., https://www.philips.com
 * SPDX-License-Identifier: MIT
 */

package com.philips.research.bombase.core.pypi.domain;

import pl.tlinkowski.annotation.basic.NullOr;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PyPiAPI {
    @GET("pypi/{project}/{version}/json")
    Call<ResponseJson> getDefinition(@Path("project") String project, @Path("version") String version);

    class ResponseJson implements ReleaseDefinition {
        @NullOr String release = null;
        @SuppressWarnings("NotNullFieldNotInitialized")
        InfoJson info;
        Map<String, List<FileJson>> releases = new HashMap<>();

        @Override
        public Optional<String> getName() {
            return Optional.ofNullable(info.name);
        }

        @Override
        public Optional<String> getSummary() {
            return Optional.ofNullable(info.summary);
        }

        @Override
        public Optional<URI> getHomepage() {
            return Optional.ofNullable(info.homePage);
        }

        @Override
        public Optional<String> getLicense() {
            return Optional.ofNullable(info.license);
        }

        @Override
        public Optional<String> getSourceUrl() {
            return releases.getOrDefault(release, List.of()).stream()
                    .filter(file -> "sdist".equals(file.packagetype))
                    .map(file -> file.url)
                    .findAny();
        }
    }

    class InfoJson {
        @NullOr String name;
        @NullOr String summary;
        @NullOr URI homePage;
        @NullOr String license;
    }

    class FileJson {
        @NullOr String packagetype;
        @NullOr String url;
    }
}
