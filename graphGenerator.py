import pandas as pd
import matplotlib.pyplot as plt

# Lee el archivo de datos
filename = 'data.txt'
data = pd.read_csv(filename, skiprows=1)

# Procesa la columna 'found' para determinar los colores
colors = data['found'].map({False: 'red', True: 'green'})

# Configura el ancho de las barras
bar_width = 0.8

# Genera un índice para los valores de K para asegurar espacio equidistante
indices = range(len(data['k']))

# Gráfico de barras para load time
plt.figure(figsize=(12, 6))
plt.bar(indices, data['load_time(s)'], color=colors, width=bar_width)
plt.xlabel('K')
plt.ylabel('Load Time (s)')
plt.title('Load Time vs K')
plt.xticks(ticks=indices, labels=data['k'], rotation=45)
plt.tight_layout()
plt.savefig('load_time_vs_k.png')
plt.show()

# Gráfico de barras para lookup time
plt.figure(figsize=(12, 6))
plt.bar(indices, data['lookup_time(s)'], color=colors, width=bar_width)
plt.xlabel('K')
plt.ylabel('Lookup Time (s)')
plt.title('Lookup Time vs K')
plt.xticks(ticks=indices, labels=data['k'], rotation=45)
plt.tight_layout()
plt.savefig('lookup_time_vs_k.png')
plt.show()
