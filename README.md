# `ring-json-params`

[Ring](http://github.com/mmcgrana/ring) middleware that augments `:params` according to a parsed JSON request body.

Takes optional parameter `json-key`, if given wraps the JSON parameters in it's own map: `{json-key parsed-json}`. (Useful if the JSON expected does not merge into a map elegantly, for example an array of numbers.)