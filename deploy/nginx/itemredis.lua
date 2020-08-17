local args = ngx.req.get_uri_args()
local id = args["id"]
local redis = require "resty.redis"
local cache = redis:new()
local ok,err = cache:connect("172.18.205.92",6379)
local item_model = cache:get("item_"..id)
if item_model == ngx.null or item_model == nil then
	local resp = ngx.location.capture("/item/get?id="..id)
	item_model = resp.body
end

ngx.say(item_model)
