set appManager [$AdminControl queryNames type=ApplicationManager,*]
foreach app [$AdminApp list] {
	if {$app == "PlanSponsor" } {
		puts "Stoppping $app"
		$AdminControl invoke $appManager stopApplication $app
	}
	if {$app == "PlanSponsorCMA" } {
		puts "Stoppping $app"
		$AdminControl invoke $appManager stopApplication $app
	}

}

