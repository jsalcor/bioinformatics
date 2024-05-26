import random
import string

# archivo que genera un "genoma" de secuencias aleatorias sinteticamente
# para probar el codigo, ya que tenemos que probarlo tanto con el dataset original de la bacteria Euchechiria Coli 
# como con uno generado sinteticamente por nosotros de longitud 10^3-10^7 caracteres/bases nitrogenadas

# ya he generado uno y lo he depositado en /datasets :)

def generate_random_sequence(length):
    """Genera una secuencia de ADN aleatoria de longitud especificada."""
    return ''.join(random.choices('ACGT', k=length))

def write_fasta(filename, header, sequence):
    """Escribe una secuencia en formato FASTA a un archivo."""
    with open(filename, 'w') as f:
        f.write(f">{header}\n")
        for i in range(0, len(sequence), 80):
            f.write(sequence[i:i+80] + '\n')

def generate_fasta_file(filename, sequence_length):
    """Genera un archivo FASTA con una secuencia de longitud especificada."""
    header = "synthetic_sequence"
    sequence = generate_random_sequence(sequence_length)
    write_fasta(filename, header, sequence)

# Configuración: longitud de la secuencia entre 10^3 y 10^7 caracteres
sequence_length = random.randint(10**3, 10**4)
filename = "synthetic_genome.fna"

# Generar el archivo FASTA
generate_fasta_file(filename, sequence_length)

print(f"Archivo {filename} generado con una secuencia de {sequence_length} caracteres.")
