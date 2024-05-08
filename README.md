# Familiarity as a Rule of Attraction: Simulating Mate-Search Dynamics Across Diverse Sexual Orientations 

## Background information
Agent-based models (ABMs) use code to simulate rules and interactions between individuals to understand the emergent behaviors of a population. Familiarity is a critical component of attraction, along with similarity and proximity (Reis et al., 2011). Previous ABM studies have shown that attraction yields greater mated pair numbers than similarity, but those studies do not include familiarity as a viable mating strategy. We aim to bridge the gap in the literature by highlighting familiarity as a rule of attraction among diverse sexual orientations.

**Research Question:** How does familiarity influence mate search with an attraction rule compared to similarity in attractiveness?
**Hypothesis:** Familiarity levels will increase towards the end of the simulation regardless of mating strategy.

## Methods
Agents are assigned a gender, a sexuality (lesbian, gay, bisexual, and straight), and an attractiveness score between 0 and 1 based on some beta distribution (either uniform or approximately normal). All agents are then spawned into a nxn grid. The grid is sectioned off into neighborhoods which determine initial familiarity scores between agents placed in the same starting neighborhood. Agents will move around randomly in the space until they encounter another agent, where they will then check compatibility, interact, and decide whether or not to reject or accept the opposing agent. If agents decide to pair up, they will be removed from the simulation. The simulation will end once all agents are removed from the simulation or once the simulation reaches the max number of step counts. A single simulation will assess either similarity or attraction rule. 

![abm process diagram (1)](https://github.com/cath-chen/mate-choice-abm/assets/97262773/c868000a-5201-44e3-8ca2-0505db11904e)

### Atraction Rule
According to the attraction rule, agents will try to pair with agents with high attraction levels. A successful match happens when both agents are above each others threshold values. If any of the agents do not meet the other agent's threshold, they will be rejected. The rejected agent's preference threshold will get slightly bigger by 0.001, allowing more tolerance to less attractive mates in future steps. 

### Similarity Rule
The similarity rule is based on the attraction values of each agent. In the simulation, similarity is the difference between the attractiveness values of the agents. The smaller this value is, the more similar the agents are. Compatible agents will decide whether or not to pair up if the agents are within each others similarity threshold. If any of the agents do not meet the other's threshold, they will be rejected. The rejected agent's similarity threshold will get wider by 0.0005, allowing for a bigger difference in similarity. 

### Familiarity
When familiarity is run in the simulation, familiarity is added to the preference threshold, widening the range of acceptance for each agent. When agents reject each other for either rule, they will gain familiarity. As agents interact with each other over and over again, their familiarity score will increase, making it easier for the agents to accept each other, no matter the attraction or familiarity score. 

![Decision diagram](https://github.com/cath-chen/mate-choice-abm/assets/97262773/0fb73edd-d734-4847-b8fb-c49d93c1fec5)

## Simulation
Our simulations are written in Java and run in Eclipse. It uses the Mason and Mason9 simulation toolkits to run the simulations. It uses the `script.txt` file to set parameters for experiments. Parameters can be sweeped, which means each parameter can be run with multiple values and a combination of all the parameters with different values will be run. A maximum of 3 parameters can be run at a time.

### Parameters
- `gridWidth` - width of grid
- `gridHeight` - height of grid
- `neighborhoodSize` - number of neighborhoods in the grid size
- `total` - number of agents in the simulation
- `searchRadius` - determines area around the agent to find other agents to interact with
- `attract_similar` - attraction rule when true, similarity rule when false
- `familiar` - includes familiarity in simulation
- `movementSize` - determines how far an agent moves each step
- `p` - probability that an agent changes direction
- `threshold` - determines initial threshold for all agents
- `alpha_a` - alpha value for attraction distribution
- `beta_a` - beta value in beta distribution for attraction distribution
- `alpha_s` - alpha value in beta distribution for similarity distribution
- `beta_s` - beta value in beta distribution for similarity distribution
- `simLength` - simulation length
- `simNumber` - number of simulations per parameter sweep

### Experiments and Parameters Sweeped 
[to be written]

## Results
In simulations where attraction is the main mating rule, familiarity is gained much faster and has a greater impact on the decision of mate choice, as all attraction simulations end with nearly 3x as much familiarity as similarity trials. Where similarity is considered, familiarity levels are low and remain constant over time, regardless of distribution. This indicates that familiarity is not important when dealing with similarity mating strategies. Normal distribution leads to greater differences in attraction values between mated pairs in the beginning of the simulation. Differences in attraction are quickly narrowed when the distribution is normal but remain consistent over time when the distribution is uniform.

### Contributors
Catherine Chen
Sydney Gonzales
Sydney Wood
Carson Chiem
Elina Moreno
Isaac Craig

PSC 120 Winter 2024
