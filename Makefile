.PHONY: test

test:
	clojure -A:dev -m kaocha.runner
test_release:
	cd ./example/re-frame && yarn shadow-cljs release app
	cd ./example/fulcro3 && yarn shadow-cljs release app
updata_all_deps:
	clojure -Aoutdated -a outdated  --update
	cd ./example/re-frame && yarn upgrade-interactive; clojure -Aoutdated -a outdated  --update
	cd ./example/fulcro3 && yarn upgrade-interactive; clojure -Aoutdated -a outdated  --update
	clojure -Spom
deploy:
	mvn deploy
