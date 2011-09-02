# Copyright 2002-2011 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License"); you may not use
# this file except in compliance with the License. You may obtain a copy of the
# License at http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software distributed
# under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
# CONDITIONS OF ANY KIND, either express or implied. See the License for the
# specific language governing permissions and limitations under the License.

require "core/http"
require "core/nodex"
include Http

Nodex::go{
  client = Client.new
  client.port = 8080
  client.host = "localhost"
  client.get_now("/") { |resp|
    puts "Got response #{resp.status_code}"
    resp.data_handler{ |buffer|
      puts "Got data #{buffer}"
    }
  }
}

puts "hit enter to exit"
STDIN.gets