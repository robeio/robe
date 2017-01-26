# Change Log

## 0.5.1.0-beta.18
* added alias to ORDER BY

## 0.5.1.0-beta.17
* GROUP BY query has been moved before ORDER BY query.

## 0.5.1.0-beta.16
* fixed restrictions name suffix increment problem

## 0.5.1.0-beta.15
* fixed obtaining multiple transient-relation members in the DTO Entity from the HQL Select Fields.
* fixed wrong variable name of the parameters in the HQL.

## 0.5.1.0-beta.14
* Fixed multiple filtering on the same field.
* Fixed Conversion of the Collections in the HQL Query (setParameterList)

## 0.5.1.0-beta.13
*  changed test port as 8686
* valueAlias of restrictions is optional now.
* Criteria alias and JoinCriteria alias is optional now.

## 0.5.1.0-beta.12
* fixed bugs.

## 0.5.1.0-beta.11
* fixed Q Filter Problem

## 0.5.1.0-beta.10
* changed alias name of fields of Root Entity as field name.
* tested pairList strict,  dto and map results.

## 0.5.1.0-beta.9
* Fixed path names of Criteria API.
* Fixed some performance problems.

## 0.5.1.0-beta.8
* added automatic select fields on DTO Classes ( Entities can be DTO if needed some transient members.)
* Fixed Offset and Limit problem.

## 0.5.1.0-beta.7
* create new Criteria and Query Api To query as Generic by Given TransgormerImpl.
* create HQL Transformer Impl to convert Criteria to HQL query.

## 0.5.1.0-beta.6
* changed Search Query JOIN as LEFT OUTER JOIN as Standart.

## 0.5.1.0-beta.5
* fixed Hibernate Search Query Api wrong Query.

## 0.5.1.0-beta.4
* fixed Hibernate Search Query Api missing space query "AND"

## 0.5.1.0-beta.3
* fixed Hibernate Search Query Api Oid Problem.
* fixed Hibernate Search Query Api Sorting Problem.
* added control to check JCE Security Algorithm Packages to use auth-bundle. 

