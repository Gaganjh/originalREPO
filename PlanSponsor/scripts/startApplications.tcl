set appManager [$AdminControl queryNames type=ApplicationManager,*]
foreach app [$AdminApp list] {
	if {$app == "PlanSponsor" } {
		$AdminControl invoke $appManager startApplication $app
	}
	if {$app == "PlanSponsorCMA" } {
		$AdminControl invoke $appManager startApplication $app
	}

}

