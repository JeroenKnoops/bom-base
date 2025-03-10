/*
 * Copyright (c) 2020-2021, Koninklijke Philips N.V., https://www.philips.com
 * SPDX-License-Identifier: MIT
 */

package com.philips.research.bombase.core.npm.domain;

import com.fasterxml.jackson.databind.JsonNode;
import pl.tlinkowski.annotation.basic.NullOr;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface NpmAPI {
    @GET("{project}/{version}")
    Call<ResponseJson> getDefinition(@Path("project") String project,
                                     @Path("version") String version);

    @SuppressWarnings("NotNullFieldNotInitialized")
    class ResponseJson implements PackageDefinition {
        @NullOr String name;
        @NullOr String description;
        @NullOr URI homePage;
        @NullOr JsonNode license;
        @NullOr JsonNode author;
        @NullOr JsonNode repository;
        DistJson dist;

        @Override
        public Optional<String> getName() {
            return Optional.ofNullable(name);
        }

        @Override
        public Optional<String> getDescription() {
            return Optional.ofNullable(description);
        }

        @Override
        public Optional<List<String>> getAuthors() {
            if (author == null) {
                return Optional.empty();
            }
            final var result = author.findValuesAsText("name");
            return Optional.of(result);
        }

        @Override
        public Optional<URI> getHomepage() {
            return Optional.ofNullable(homePage);
        }

        @Override
        public Optional<String> getLicense() {
            return Optional.ofNullable(licenseOf(license));
        }

        @NullOr String licenseOf(@NullOr JsonNode node) {
            if (node == null) {
                return null;
            }
            if (node.isArray()) {
                return StreamSupport.stream(node.spliterator(), false)
                        .map(this::licenseOf)
                        .collect(Collectors.joining(" AND ")); // NPM provides insufficient info
            }
            if (node.isObject()) {
                return node.get("type").textValue();
            }
            return node.textValue();
        }

        @Override
        public Optional<String> getSourceUrl() {
            if (repository == null) {
                return Optional.empty();
            }
            if (repository.isObject()) {
                return Optional.ofNullable(repository.get("url").textValue());
            }
            return Optional.of(repository.textValue());
        }

        @Override
        public Optional<URI> getDownloadUrl() {
            return Optional.ofNullable(dist.tarball);
        }

        @Override
        public Optional<String> getSha() {
            return Optional.ofNullable(dist.shasum);
        }
    }

    class DistJson {
        @NullOr URI tarball;
        @NullOr String shasum;
    }
}
