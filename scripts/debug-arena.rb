#!/usr/bin/env ruby

HOME_DIR = File.expand_path(File.dirname(__FILE__) + "/..")

require 'optparse'

java_opts = ["-J-DprojectDir=#{HOME_DIR}", '-J-DdevelopmentMode=true']

ARGV.options do |opts|
	opts.banner = "Usage:  #{File.basename($PROGRAM_NAME)} [OPTIONS]"
	
	opts.separator ""
	opts.separator "Common Options:"
	
	opts.on( "-h", "--help",
	         "Show this message." ) do
		puts opts
		exit
	end

	opts.on( "-i", "--idea-debug",
			 "Enable idea remote debugging while running the arena" ) do
		java_opts << '-J-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005'
	end

	begin
		opts.parse!
	rescue
		puts opts
		exit
	end
end

puts "Executing javaws with argumnets: "
puts java_opts.map{ |x| "  " + x }.join "\n"
system "javaws #{java_opts.join ' '} #{HOME_DIR}/scripts/ContestAppletProd.jnlp"
