foreach app [$AdminApp list] {
	if {$app == "PlanSponsor" } {
		$AdminApp uninstall $app
		$AdminConfig save 
	}
	if {$app == "PlanSponsorCMA" } {
		$AdminApp uninstall $app
		$AdminConfig save 
	}

}