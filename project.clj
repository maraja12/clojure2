(defproject clojureapp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.csv "0.1.2"]
                 [org.clojure/data.json "1.0.0"]
                 [midje "1.10.10"]
                 [seancorfield/next.jdbc "1.2.659"]
                 [com.h2database/h2 "1.4.200"]
                 ]
  :repl-options {:init-ns clojureapp.core})
