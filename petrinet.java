The amazing sequence diagram and code

m_i - number of missing tokens
r_i - number of remaining tokens
c_i - number of consumed tokens
p_i - number of produced tokens during log replay of the current trace
k - number of different traces in log - len(cases)
n_i - number of process instances combined into current trace - len(trace)
x_i - mean number of enabled transitions during log replay of the current trace - mean(num_of_enabled_transitions)
N - the set of nodes (i.e. places and transitions) in the Petri net model - transitions.length() + places.length()

input:
Petrinet input_petrinet
Log log

List <Place> places = input_petrinet.getPlaces()
List <Transition> transitions = input_petrinet.getTransitions()
Integer T_v = transitions.length()
Integer N = transitions.length() + places.length()

List <Case> cases = log.getCases()
Integer k = cases.length()

List <Integer> n;
List <Integer> m;
List <Integer> c;
List <Integer> p;
List <Integer> x;

for each case in cases: # 1207: ABDEA

	Petrinet petrinet = new Petrinet(places, transitions)
	Trace trace = case.getTrace() # ABDEA
	Integer n_i = trace.length()
	Integer L_i = trace.length()
	
	Integer num_of_enabled_transitions = 0 # later x_i = num_of_enabled_transitions / n_i
	
	# Start of first step
	
	# Add the first token
	currentPlace = petrinet.getStartPlace()
	currentPlace.produceToken()
	p_i ++

	List <Transition> outTransitions = currentPlace.getOutGoingTransitions()
	
	# enable necessary transitions
	for outT in outTransitions:
		List <Place> inPlaces = outT.getInComingPlaces()
		enabled = True
		for inPlace in inPlaces:
			if inPlace.hasToken() == False:
				enabled = False
		if enabled:
			outT.setEnabled(enabled)
			num_of_enabled_transitions++
	
	# Semi-end of first step
	
	List <Event> events = trace.getEvents()
	
	for each event in events: # A
		
		String label = event.getLabel()
		Transition transition = transitions.getTransition(label)
		
		# end of first step
		# start of second step
		
		if transition.isEnabled():
			currentPlace.consumeToken())
			c_i ++
			transition.setEnabled(False)
			num_of_enabled_transitions--
		else:
			m_i ++
		List <Place> outPlaces = transition.getOutGoingPlaces()
		# produce tokens
		for each outPlace in outPlaces:
			outPlace.produceToken()
			p_i ++
			
		# (dis)enable transitions
		for each outPlace in outPlaces:
			List <Transition> outTransitions = outPlace.getOutGoingTransitions()
			for each outT in outTransitions:
				enabled = True
				List <Place> inPlaces = outT.getInComingPlaces()
				for inPlace in inPlaces:
					if not inPlace.hasToken():
						enabled = False
				outT.setEnabled(enabled)
				
		num_of_enabled_transitions = petrinet.getNumberOfEnabledTokens()
		
		# End of step
					
	Integer r_i = petrinet.getNumberOfTokens()
	
	# add to lists
	n.add(n_i)
	r.add(r_i)
	p.add(p_i)
	c.add(c_i)
	x.add(num_of_enabled_transitions / n_i)


# TODO:
# Kas võib eeldada, et alguses/kogu petrinetis ei ole ühtegi tokenit?
# WTF L?
# Kas traces on esimene event alati start place? Hetkel eeldame