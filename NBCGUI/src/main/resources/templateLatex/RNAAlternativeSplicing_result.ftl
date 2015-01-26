\subsubsection{Alternative Splicing Analysis:}
Group：${groupName}
Alternative splicing analysis was constructed to discover the difference of each samples group and discover several important alternative splicing genes. Following (Table <#if table??>\ref{${table.label}</#if>}) was the alternative splicing result of compare ${groupName}.

<#if table??>
	\begin{table}[h]
	  \centering
	  <#if table.tableTitle??>
	  \caption{${table.tableTitle}}\label{${table.label}}
	  </#if>
        <#if table.lsLsData??>
          <#assign i=(table.lsLsData[0]?size-1)/table.columnNum>
          <#assign i=i?ceiling>
          <#list 0..i-1 as t>
        	  \begin{tabular}{cccccccccccccc}
        	    \hline
                <#list table.lsLsData as lsData>
                	<#assign row=table.lsFirstColumn[lsData_index]+" & ">
            		<#list 1..table.columnNum as j>
            				<#assign data="">
                     <#if lsData[t*table.columnNum+j]??>
            					<#assign data=lsData[t*table.columnNum+j]>
            			<#else>
            					<#assign data="">
            			</#if>
        				<#if j==table.columnNum>
        				<#assign row=row+data+" \\\\">
        				<#else>
        				<#assign row=row+data+" & ">
        				</#if>
            		</#list>
            		<#if lsData_index==0>
            				<#assign row=row+"\\hline">		
            		</#if>
                	${row}
                </#list>
        	    \hline
        	  \end{tabular}
          </#list>
        </#if>
	\end{table}
</#if>

Moreover, Novelbio compare the alternaive splicing situation of S36 strain and WT stain in different time point.