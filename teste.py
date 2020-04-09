import requests, sys

a = open("domainX.txt", 'r').read()
b = open("problemX.txt", 'r').read()

print (sys.argv)
data = {'domain': a,
        'problem': b}

resp = requests.post('http://solver.planning.domains/solve',
                     verify=False, json=data).json()


with open("saidaX", 'w') as f:
    f.write('\n'.join([act['name'] for act in resp['result']['plan']]))

