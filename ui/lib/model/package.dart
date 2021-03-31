/*
 * Copyright (c) 2020-2021, Koninklijke Philips N.V., https://www.philips.com
 * SPDX-License-Identifier: MIT
 */

class Package {
  Package({required this.id, required this.purl, required this.updated});

  final String id;
  final Uri purl;
  final DateTime updated;
  String? title;
  String? description;
  Uri? sourceLocation;
  Uri? downloadLocation;
  String? declaredLicense;
  List<String>? detectedLicenses;
}
