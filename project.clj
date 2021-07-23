(defproject tupelo/neo4j "0.1.0"
  :description   "Clojure bindings for Neo4j using the Java driver & Bolt protocol"
  :license       {:name "Eclipse Public License"
                  :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins       [[com.jakemccrary/lein-test-refresh "0.24.1"]
                  [lein-ancient "0.7.0"]
                  [lein-environ "1.2.0"]]

  :dependencies  [
                  [clj-time "0.15.2"]
                  [environ "1.2.0"]
                  [org.clojure/clojure "1.10.3"]
                  [org.neo4j.driver/neo4j-java-driver "4.3.3"]
                  [prismatic/schema "1.1.12"]
                  [tupelo "21.07.08"]
                 ]

  :profiles      {:provided      {:dependencies [[org.neo4j.test/neo4j-harness "4.0.0"]]}

                  :profiles/dev  {} ; *** overwritten from profiles.clj ***
                  :profiles/test {} ; *** overwritten from profiles.clj ***

                  :project/dev   {}
                  :project/test  {}

                  ; merge, last one wins
                  :dev           [:project/dev :profiles/dev]
                  :test          [:project/test :profiles/test]
                 }

  :global-vars   {*warn-on-reflection* false}

  :target-path   "target/%s"
  :compile-path  "%s/class-files"
  :clean-targets [:target-path]

  :jvm-opts      ["-Xms500m" "-Xmx2g"]

)

