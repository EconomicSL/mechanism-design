/*
Copyright 2017 EconomicSL

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.economicsl

import java.util.UUID

import scala.collection.{GenMap, GenSet}


package object mechanisms {

  /** Base trait for representing various alternative outcomes generated by some Mechanism. */
  trait Alternative


  /** Base trait for representing preferences defined over a particular type of `Alternative`. */
  trait Preference[A <: Alternative] {

    /** Formally, preferences are represented as an `Ordering` defined over a type of `Alternative`. */
    def ordering: Ordering[A]

  }

  /** Base trait defining a generic social welfare function.
    *
    * A social welfare function aggregates the preferences of individual agents into a common preference ordering.
    */
  trait SocialWelfareFunction[P <: Preference[_ <: Alternative]] extends ((GenMap[UUID, P]) => P)


  /** Base trait defining a generic social choice function.
    *
    * A social choice function aggregates the preferences of individual agents into the choice of a single alternative.
    */
  trait SocialChoiceFunction[A <: Alternative, P <: Preference[A]] extends ((GenSet[A], GenMap[UUID, P]) => A)


}
