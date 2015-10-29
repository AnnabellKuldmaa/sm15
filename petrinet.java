The amazing sequence diagram and code

m_i - number of missing tokens
r_i - number of remaining tokens
c_i - number of consumed tokens
p_i - number of produced tokens during log replay of the current trace
k - number of different traces in log - len(cases)
n_i - number of process instances combined into current trace - len(trace)
x_i - mean number of enabled transitions during log replay of the current trace - mean(num_of_enabled_transitions)
N - the set of nodes (i.e. places and transitions) in the Petri net model - transitions.length() + places.length()
L -s the set of distinct labels associated to transitions. In your case, you cannot have two transitions with the same label in the Petri net, so the number of labels coincide with the number of transitions.

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
List <Integer> r;
List <Integer> c;
List <Integer> p;
List <Integer> x;

for each case in cases: # 1207: ABDEA

	Petrinet petrinet = new Petrinet(places, transitions)
	Trace trace = case.getTrace() # ABDEA
	Integer n_i = trace.length()
	Integer L_i = trace.length()
	
	#Integer num_of_enabled_transitions = 0 # later x_i = num_of_enabled_transitions / n_i
	
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
			#num_of_enabled_transitions++
	
	# Semi-end of first step
	
	List <Event> events = trace.getEvents()
	
	for each event in events: # A
		
		String label = event.getName()
		Transition transition = transitions.getTransition(label)
		
		# end of first step
		# start of second step
		
		if transition.isEnabled():
			currentPlace.consumeToken())
			c_i ++
			transition.setEnabled(False)
			#num_of_enabled_transitions--
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
				for each inP in inPlaces:
					if not inP.hasTokens():
						enabled = False
				outT.setEnabled(enabled)
				
		num_of_enabled_transitions = petrinet.getNumberOfEnabledTokens()
		
		# End of step
					
	Integer r_i = petrinet.getNumberOfTokens()
	
	# add to lists
	n.add(n_i)
	m.add(m_i)
	r.add(r_i)
	p.add(p_i)
	c.add(c_i)
	x.add(num_of_enabled_transitions / n_i)

	
	

# TODO:
# Kas traces on esimene event alati start place? Hetkel eeldame. NB. EI PEA OLEMA
# Lõputeema: kui läbimise lõpus pole viimases kohas tokenit, tuleb see sinna panna ja suurendada missing tokenite arvu

# veel steppe peame lugema event logi järgi. 
# esimene step:
#paneme algusesse tokeni. enableme vajalikud transitionid ja läbime

#RESOLVED:
# Kas võib eeldada, et alguses/kogu petrinetis ei ole ühtegi tokenit? Jah, ühtegi tokenit ei ole alguses
# WTF L?: erineate labelite arv lggis

# f = 0.5*(1 - (4*0 + 3*0) / (4*4 + 3*3)) + 0.5*(1 - (4*0 + 3*0) / (4*4 + 3*3)) = 1.0